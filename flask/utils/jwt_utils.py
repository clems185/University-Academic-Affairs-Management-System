"""
JWT工具函数
用于解析token获取用户信息
"""
import jwt
from config import Config
from typing import Optional, Dict


def decode_token(token: str) -> Optional[Dict]:
    """
    解码JWT token
    Args:
        token: JWT token字符串
    Returns:
        解码后的payload字典，失败返回None
    """
    try:
        # 移除Bearer前缀（如果有）
        if token.startswith('Bearer '):
            token = token[7:]
        
        payload = jwt.decode(
            token,
            Config.JWT_SECRET_KEY,
            algorithms=[Config.JWT_ALGORITHM]
        )
        return payload
    except jwt.ExpiredSignatureError:
        print("Token已过期")
        return None
    except jwt.InvalidTokenError as e:
        print(f"Token无效: {e}")
        return None
    except Exception as e:
        print(f"解析Token失败: {e}")
        return None


def get_user_id_from_token(token: str) -> Optional[str]:
    """
    从token中提取用户ID（学号或工号）
    Args:
        token: JWT token字符串
    Returns:
        用户ID，失败返回None
    """
    payload = decode_token(token)
    if not payload:
        return None
    
    # 尝试多种可能的字段名
    user_id = (
        payload.get('userId') or
        payload.get('user_id') or
        payload.get('id') or
        payload.get('studentId') or
        payload.get('teacherId') or
        payload.get('adminId') or
        payload.get('sub')  # JWT标准字段
    )
    
    return str(user_id) if user_id else None

