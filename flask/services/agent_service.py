"""
智能体服务模块
使用LangChain和DeepSeek模型，集成RAG功能
"""
import uuid
import os
from typing import List, Dict, Optional

from langchain_classic.memory import ConversationBufferMemory

from langchain_deepseek import ChatDeepSeek
from langchain_core.messages import HumanMessage, AIMessage, SystemMessage
from services.rag_service import get_rag_service


class AgentService:
    """智能体服务类"""

    def __init__(self, agent_type: str = 'dash'):
        """
        初始化智能体服务
        Args:
            agent_type: 智能体类型 'dash' 或 'message'
        """
        self.agent_type = agent_type

        self.llm = ChatDeepSeek(model="deepseek-chat")

        # 使用字典存储每个会话的记忆
        self.memories: Dict[str, ConversationBufferMemory] = {}

        # 初始化RAG服务（仅对dash类型的agent使用RAG）
        self.rag_service = None
        if agent_type == 'dash':
            try:
                self.rag_service = get_rag_service()
                # 延迟初始化，避免启动时阻塞
            except Exception as e:
                print(f"RAG服务初始化失败: {e}")

        # 根据类型设置系统提示词
        if agent_type == 'dash':
            self.system_prompt = """你是一个智能教务系统助手，专门帮助用户解答关于教务管理的问题。
你可以帮助用户：
1. 查询课程安排、考试信息、成绩等
2. 解答系统使用问题
3. 提供教务相关的建议和帮助
4. 根据数据库结构信息，理解用户的问题并给出准确的回答

重要提示：
- 你会收到数据库结构信息作为参考，请基于这些信息准确回答用户的问题
- 如果用户询问具体数据（如"我的成绩"、"我的课程"等），请基于数据库结构信息理解表之间的关系
- 回答要准确、专业，如果信息不足，请说明需要哪些信息才能查询

请用友好、专业的语气回答用户的问题。"""
        elif agent_type == 'message':
            self.system_prompt = """你是一个智能消息审核助手，专门帮助管理员审核消息申请。
你的职责是：
1. 分析待审核的消息申请内容
2. 判断申请是否符合发布标准
3. 提供审核建议（通过/拒绝/需要更多信息）
4. 生成审核意见

请仔细分析申请内容，包括标题、内容、类型等，给出专业的审核建议。
如果建议通过，请提供审核意见；如果建议拒绝，请说明拒绝理由。"""

    def _get_memory(self, conversation_id: str) -> ConversationBufferMemory:
        """获取或创建会话记忆"""
        if conversation_id not in self.memories:
            self.memories[conversation_id] = ConversationBufferMemory(
                return_messages=True
            )
        return self.memories[conversation_id]

    def _build_context_for_message_agent(self, pending_applications: List[Dict]) -> str:
        """为消息审核智能体构建上下文"""
        if not pending_applications:
            return "当前没有待审核的申请。"

        context = f"当前有 {len(pending_applications)} 条待审核申请：\n\n"
        for i, app in enumerate(pending_applications, 1):
            context += f"申请 {i}：\n"
            context += f"  - ID: {app.get('InfoApplyId', 'N/A')}\n"
            context += f"  - 标题: {app.get('Title', 'N/A')}\n"
            context += f"  - 类型: {app.get('Types', 'N/A')}\n"
            context += f"  - 内容: {app.get('Content', 'N/A')[:200]}...\n"  # 限制内容长度
            context += f"  - 申请时间: {app.get('ApplyTime', 'N/A')}\n"
            context += f"  - 申请人ID: {app.get('ApplicantId', 'N/A')}\n\n"

        return context

    def _extract_suggestion(self, response_text: str) -> Optional[Dict]:
        """从响应中提取建议操作（简单实现，实际可以使用更复杂的解析）"""
        # 这里可以根据实际需求实现更复杂的逻辑
        # 例如使用正则表达式或LLM再次解析
        response_lower = response_text.lower()

        suggestion = None
        if '建议通过' in response_text or '建议批准' in response_text or '可以发布' in response_text:
            suggestion = {
                'action': 'approve',
                'reason': '内容符合发布标准',
                'reviewComments': '经审核，内容符合发布要求，建议通过。'
            }
        elif '建议拒绝' in response_text or '不建议通过' in response_text or '不符合' in response_text:
            suggestion = {
                'action': 'reject',
                'reason': '内容不符合发布标准',
                'reviewComments': '经审核，内容不符合发布要求，建议拒绝。'
            }
        elif '需要更多信息' in response_text or '信息不足' in response_text:
            suggestion = {
                'action': 'need_more_info',
                'reason': '需要更多信息才能做出判断',
                'reviewComments': '申请信息不够完整，需要补充更多信息。'
            }

        return suggestion

    def chat(self, question: str, user_id: str = '', conversation_id: str = '',
             pending_applications: Optional[List[Dict]] = None) -> Dict:
        """
        处理聊天请求
        Args:
            question: 用户问题
            user_id: 用户ID（学号或工号）
            conversation_id: 会话ID
            pending_applications: 待审核申请列表（仅用于消息审核智能体）
        Returns:
            包含回答和建议的字典
        """
        # 生成或使用会话ID
        if not conversation_id:
            conversation_id = str(uuid.uuid4())

        # 获取会话记忆
        memory = self._get_memory(conversation_id)

        # 构建消息列表
        messages = [SystemMessage(content=self.system_prompt)]

        # 添加历史对话
        if memory.chat_memory.messages:
            messages.extend(memory.chat_memory.messages[-10:])  # 只保留最近10轮对话



        # 为消息审核智能体添加上下文
        if self.agent_type == 'message' and pending_applications:
            context = self._build_context_for_message_agent(pending_applications)
            messages.append(SystemMessage(content=f"上下文信息：\n{context}"))

        # 添加用户问题
        messages.append(HumanMessage(content=question))

        try:
            # 调用LLM
            response = self.llm.invoke(messages)
            answer = response.content if hasattr(response, 'content') else str(response)

            # 更新记忆
            memory.chat_memory.add_user_message(question)
            memory.chat_memory.add_ai_message(answer)

            # 提取建议（仅消息审核智能体）
            suggested_action = None
            if self.agent_type == 'message':
                suggested_action = self._extract_suggestion(answer)

            result = {
                'answer': answer,
                'conversationId': conversation_id
            }

            if suggested_action:
                result['suggestedAction'] = suggested_action

            return result

        except Exception as e:
            print(f"LLM调用错误: {e}")
            # 返回错误信息
            return {
                'answer': f'抱歉，处理您的问题时出现了错误：{str(e)}。请稍后重试。',
                'conversationId': conversation_id
            }

