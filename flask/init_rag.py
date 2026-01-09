"""
RAG初始化脚本
用于将数据库schema从SQL文件解析并向量化存储到Milvus
"""
import sys
import os

# 添加项目根目录到路径
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from services.rag_service import get_rag_service


def main():
    """初始化RAG向量库"""
    print("=" * 50)
    print("开始初始化RAG向量库...")
    print("=" * 50)
    
    try:
        # 获取RAG服务实例
        rag_service = get_rag_service()
        
        # 初始化（如果集合已存在，不会重建；如果需要重建，设置force_rebuild=True）
        force_rebuild = False
        if len(sys.argv) > 1 and sys.argv[1] == '--rebuild':
            force_rebuild = True
            print("警告: 将重建向量库，现有数据将被删除！")
        
        rag_service.initialize(force_rebuild=force_rebuild)
        
        print("=" * 50)
        print("RAG向量库初始化完成！")
        print("=" * 50)
        
    except Exception as e:
        print(f"初始化失败: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)


if __name__ == '__main__':
    main()

