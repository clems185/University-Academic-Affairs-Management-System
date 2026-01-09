"""
数据库连接模块
"""
import pymysql
from config import Config

# 全局数据库连接池
_db_pool = None

def get_db_connection():
    """
    获取数据库连接
    返回: pymysql连接对象
    """
    try:
        connection = pymysql.connect(
            host=Config.DB_HOST,
            port=Config.DB_PORT,
            user=Config.DB_USER,
            password=Config.DB_PASSWORD,
            database=Config.DB_NAME,
            charset=Config.DB_CHARSET,
            cursorclass=pymysql.cursors.DictCursor,
            autocommit=True
        )
        return connection
    except Exception as e:
        print(f"数据库连接失败: {e}")
        raise

def init_db():
    """初始化数据库连接（测试连接）"""
    try:
        conn = get_db_connection()
        if conn:
            conn.close()
            print("数据库连接成功")
            return True
    except Exception as e:
        print(f"数据库初始化失败: {e}")
        return False

def execute_query(sql, params=None):
    """
    执行查询语句
    返回: 查询结果列表
    """
    conn = None
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute(sql, params)
            result = cursor.fetchall()
            return result
    except Exception as e:
        print(f"查询执行失败: {e}")
        raise
    finally:
        if conn:
            conn.close()

def execute_update(sql, params=None):
    """
    执行更新语句（INSERT, UPDATE, DELETE）
    返回: 影响的行数
    """
    conn = None
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            affected_rows = cursor.execute(sql, params)
            conn.commit()
            return affected_rows
    except Exception as e:
        if conn:
            conn.rollback()
        print(f"更新执行失败: {e}")
        raise
    finally:
        if conn:
            conn.close()

