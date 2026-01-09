"""
Flask应用主文件
提供智能体API服务
"""
from flask import Flask, request, jsonify
from flask_cors import CORS
from config import Config
from database import init_db
from routes.agentdash import agentdash_bp
from routes.agentmessage import agentmessage_bp

# 确保导入dotenv以加载环境变量
try:
    from dotenv import load_dotenv

    load_dotenv()
except ImportError:
    pass


def create_app():
    """创建并配置Flask应用"""
    app = Flask(__name__)
    app.config.from_object(Config)

    # 启用CORS，允许前端跨域请求
    CORS(app, resources={
        r"/api/*": {
            "origins": ["http://localhost:8080", "http://localhost:5173", "http://localhost:3000"],
            "methods": ["GET", "POST", "PUT", "DELETE", "OPTIONS"],
            "allow_headers": ["Content-Type", "Authorization"]
        }
    })

    # 初始化数据库
    init_db()

    # 注册蓝图
    app.register_blueprint(agentdash_bp, url_prefix='/api/agentdash')
    app.register_blueprint(agentmessage_bp, url_prefix='/api/agentmessage')

    # 健康检查端点
    @app.route('/api/health', methods=['GET'])
    def health_check():
        return jsonify({
            'status': 'ok',
            'service': 'Flask Agent Service',
            'version': '1.0.0'
        }), 200

    # 错误处理
    @app.errorhandler(404)
    def not_found(error):
        return jsonify({
            'IsSuccess': False,
            'Message': '接口不存在',
            'Result': None
        }), 404

    @app.errorhandler(500)
    def internal_error(error):
        return jsonify({
            'IsSuccess': False,
            'Message': '服务器内部错误',
            'Result': None
        }), 500

    return app


if __name__ == '__main__':
    app = create_app()
    app.run(
        host=Config.HOST,
        port=Config.PORT,
        debug=Config.DEBUG
    )

