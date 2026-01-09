"""
Flask应用配置文件
"""
import os
from dotenv import load_dotenv

# 加载环境变量
load_dotenv()


class Config:
    """应用配置类"""
    # Flask配置
    SECRET_KEY = os.environ.get('SECRET_KEY') or 'your-secret-key-here-change-in-production'
    DEBUG = os.environ.get('FLASK_DEBUG', 'True').lower() == 'true'

    # 服务器配置
    HOST = os.environ.get('FLASK_HOST', '0.0.0.0')
    PORT = int(os.environ.get('FLASK_PORT', 5000))

    # 数据库配置（与.NET后端保持一致）
    DB_HOST = os.environ.get('DB_HOST', 'localhost')
    DB_PORT = int(os.environ.get('DB_PORT', 3306))
    DB_USER = os.environ.get('DB_USER', 'root')
    DB_PASSWORD = os.environ.get('DB_PASSWORD', '123456')
    DB_NAME = os.environ.get('DB_NAME', 'sys-test')
    DB_CHARSET = os.environ.get('DB_CHARSET', 'utf8mb4')

    # JWT配置（用于验证token，如果需要）
    JWT_SECRET_KEY = os.environ.get(
        'JWT_SECRET_KEY') or 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI2a2EJ7m872v0afyoSDJT2o1+SitIeJSWtLJU8/Wz2m7gStexajkeD+Lka6DSTy8gt9UwfgVQo6uKjVLG5Ex7PiGOODVqAEghBuS7JzIYU5RvI543nNDAPfnJsas96mSA7L/mD7RTE2drj6hf3oZjJpMPZUQI/B1Qjb5H3K3PNwIDAQAB'
    JWT_ALGORITHM = 'HS256'

    # DeepSeek API配置
    DEEPSEEK_API_KEY = os.environ.get('DEEPSEEK_API_KEY', '')

    @property
    def DATABASE_URI(self):
        """构建数据库连接URI"""
        return f"mysql+pymysql://{self.DB_USER}:{self.DB_PASSWORD}@{self.DB_HOST}:{self.DB_PORT}/{self.DB_NAME}?charset={self.DB_CHARSET}"

