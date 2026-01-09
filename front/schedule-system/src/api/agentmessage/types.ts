// 智能体消息处理API类型定义

// 建议操作类型
export interface SuggestedAction {
  action: 'approve' | 'reject' | 'need_more_info';
  reason: string;
  reviewComments?: string;
}

export interface ChatMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: number;
  thinking?: boolean; // 是否正在思考
  suggestedAction?: SuggestedAction; // 建议的操作
}

export interface MessageChatRequest {
  question: string;
  userId?: string;
  conversationId?: string;
  // 可选的上下文信息，用于智能体了解当前待审核的申请
  pendingApplications?: Array<{
    InfoApplyId: string;
    Title: string;
    Content: string;
    Types: string;
    ApplyTime: string;
    ApplicantId: string;
  }>;
}

export interface MessageChatResponse {
  answer: string;
  conversationId?: string;
  thinking?: string; // 智能体思考过程
  // 智能体可能提供的建议操作
  suggestedAction?: SuggestedAction;
}

export interface AgentApiResult<T = any> {
  IsSuccess: boolean;
  Message?: string;
  Result: T;
}

