// 智能客服API类型定义

export interface ChatMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: number;
}

export interface ChatRequest {
  question: string;
  userId?: string;
  conversationId?: string;
}

export interface ChatResponse {
  answer: string;
  conversationId?: string;
  thinking?: string; // 智能体思考过程
}

export interface AgentApiResult<T = any> {
  IsSuccess: boolean;
  Message?: string;
  Result: T;
}

