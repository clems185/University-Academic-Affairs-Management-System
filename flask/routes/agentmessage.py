"""
消息审核智能助手路由
"""
from flask import Blueprint, request, jsonify
from services.agent_service import AgentService
from utils.jwt_utils import get_user_id_from_token

agentmessage_bp = Blueprint('agentmessage', __name__)


@agentmessage_bp.route('/chat', methods=['POST'])
def chat():
    """
    处理消息审核智能助手的聊天请求
    POST /api/agentmessage/chat
    """
    try:
        # 获取请求数据
        data = request.get_json()
        if not data:
            return jsonify({
                'IsSuccess': False,
                'Message': '请求数据不能为空',
                'Result': None
            }), 400

        question = data.get('question', '').strip()
        if not question:
            return jsonify({
                'IsSuccess': False,
                'Message': '问题不能为空',
                'Result': None
            }), 400

        # 获取用户ID：优先从请求参数获取，否则从token中解析
        user_id = data.get('userId', '')
        if not user_id:
            # 尝试从token中获取
            token = request.headers.get('Authorization', '')
            if token:
                user_id = get_user_id_from_token(token) or ''
        
        # 获取会话ID和待审核申请列表
        conversation_id = data.get('conversationId', '')
        pending_applications = data.get('pendingApplications', [])

        # 调用智能体服务
        agent_service = AgentService(agent_type='message')
        response = agent_service.chat(
            question=question,
            user_id=user_id,
            conversation_id=conversation_id,
            pending_applications=pending_applications
        )

        return jsonify({
            'IsSuccess': True,
            'Message': '成功',
            'Result': response
        }), 200

    except Exception as e:
        print(f"消息审核智能助手处理错误: {e}")
        return jsonify({
            'IsSuccess': False,
            'Message': f'处理请求时发生错误: {str(e)}',
            'Result': None
        }), 500

