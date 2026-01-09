"""
RAG服务模块
集成Milvus向量检索和数据库schema信息
"""
import os
from typing import List, Dict, Optional
from langchain_core.documents import Document

from milvus_store import (
    get_vector_store,
    connect_milvus,
    get_lc_retriever,
    add_texts
)
from services.database_schema_parser import DatabaseSchemaParser


class RAGService:
    """RAG服务类"""
    
    def __init__(self, collection_name: str = "academic_system_rag"):
        """
        初始化RAG服务
        Args:
            collection_name: Milvus集合名称
        """
        self.collection_name = collection_name
        self.schema_parser = DatabaseSchemaParser(sql_dir="database")
        self._initialized = False
    
    def initialize(self, force_rebuild: bool = False):
        """
        初始化RAG服务，加载数据库schema到向量库
        Args:
            force_rebuild: 是否强制重建向量库
        """
        if self._initialized and not force_rebuild:
            return
        
        try:
            connect_milvus()
            
            # 如果强制重建，先删除旧集合
            if force_rebuild:
                from pymilvus import utility
                if utility.has_collection(self.collection_name):
                    utility.drop_collection(self.collection_name)
                    print(f"已删除旧集合: {self.collection_name}")
            
            # 检查集合是否存在
            from pymilvus import utility
            collection_exists = utility.has_collection(self.collection_name)
            
            if not collection_exists or force_rebuild:
                # 解析SQL文件并创建文档
                print("正在解析数据库schema...")
                documents = self.schema_parser.create_documents_for_rag()
                
                if not documents:
                    print("警告: 未找到任何数据库schema文档")
                    # 创建空集合
                    get_vector_store(self.collection_name, force_new=force_rebuild)
                else:
                    print(f"找到 {len(documents)} 个schema文档，正在添加到向量库...")
                    # 初始化向量存储
                    vector_store = get_vector_store(self.collection_name, force_new=force_rebuild)
                    # 添加文档
                    add_texts(documents, collection_name=self.collection_name)
                    print(f"成功将 {len(documents)} 个文档添加到向量库")
            else:
                print(f"集合 {self.collection_name} 已存在，跳过初始化")
                get_vector_store(self.collection_name, force_new=False)
            
            self._initialized = True
            print("RAG服务初始化完成")
            
        except Exception as e:
            print(f"RAG服务初始化失败: {e}")
            raise
    
    def retrieve_relevant_schema(self, query: str, k: int = 3) -> str:
        """
        检索与查询相关的数据库schema信息
        Args:
            query: 用户查询
            k: 返回的文档数量
        Returns:
            相关的schema信息文本
        """
        if not self._initialized:
            self.initialize()
        
        try:
            retriever = get_lc_retriever(self.collection_name, k=k)
            documents = retriever.invoke(query)
            
            if not documents:
                return "未找到相关的数据库结构信息。"
            
            # 格式化检索结果
            formatted_results = []
            for i, doc in enumerate(documents, 1):
                formatted_results.append(f"【相关信息 {i}】\n{doc.page_content}")
            
            return "\n\n".join(formatted_results)
            
        except Exception as e:
            print(f"检索schema信息失败: {e}")
            return f"检索数据库结构信息时出错: {str(e)}"
    
    def get_user_context(self, user_id: str) -> str:
        """
        获取用户上下文信息（根据学号/工号查询用户信息）
        Args:
            user_id: 用户ID（学号或工号）
        Returns:
            用户上下文信息文本
        """
        if not user_id:
            return ""
        
        try:
            from database import execute_query
            
            # 定义可能的表名和字段映射（按优先级排序）
            user_table_configs = [
                {
                    'table': 'StudentInfo',
                    'id_field': 'StudentId',
                    'name_field': 'StudentName',
                    'role': '学生',
                    'id_label': '学号',
                    'extra_fields': ['ClassName', 'MajorName'],
                    'extra_labels': ['班级', '专业']
                },
                {
                    'table': 'TeacherInfo',
                    'id_field': 'TeacherId',
                    'name_field': 'TeacherName',
                    'role': '教师',
                    'id_label': '工号',
                    'extra_fields': ['DepartmentName'],
                    'extra_labels': ['部门']
                },
                {
                    'table': 'AdminInfo',
                    'id_field': 'AdminId',
                    'name_field': 'AdminName',
                    'role': '管理员',
                    'id_label': '工号',
                    'extra_fields': [],
                    'extra_labels': []
                }
            ]
            
            # 尝试查询每个可能的表
            for config in user_table_configs:
                try:
                    # 构建查询字段
                    select_fields = [config['id_field'], config['name_field']]
                    select_fields.extend(config['extra_fields'])
                    
                    query = f"""
                        SELECT {', '.join(select_fields)}
                        FROM {config['table']}
                        WHERE {config['id_field']} = %s
                        LIMIT 1
                    """
                    
                    result = execute_query(query, (user_id,))
                    
                    if result:
                        user = result[0]
                        # 构建用户信息描述
                        info_parts = [
                            f"当前用户是{config['role']}",
                            f"{config['id_label']}: {user.get(config['id_field'])}",
                            f"姓名: {user.get(config['name_field'])}"
                        ]
                        
                        # 添加额外字段
                        for field, label in zip(config['extra_fields'], config['extra_labels']):
                            value = user.get(field)
                            if value:
                                info_parts.append(f"{label}: {value}")
                        
                        return ", ".join(info_parts)
                        
                except Exception as e:
                    # 如果表不存在或查询失败，继续尝试下一个表
                    continue
            
            # 如果所有表都查询失败，返回基本信息
            return f"当前用户ID: {user_id}（未找到详细信息）"
            
        except Exception as e:
            print(f"获取用户上下文失败: {e}")
            return f"当前用户ID: {user_id}"
    
    def build_rag_context(self, query: str, user_id: str = "", k: int = 3) -> str:
        """
        构建RAG上下文，包括数据库schema和用户信息
        Args:
            query: 用户查询
            user_id: 用户ID（学号或工号）
            k: 检索的文档数量
        Returns:
            完整的RAG上下文
        """
        context_parts = []
        
        # 1. 用户上下文
        if user_id:
            user_context = self.get_user_context(user_id)
            if user_context:
                context_parts.append(f"【用户信息】\n{user_context}")
        
        # 2. 数据库schema信息
        schema_info = self.retrieve_relevant_schema(query, k=k)
        if schema_info:
            context_parts.append(f"【数据库结构信息】\n{schema_info}")
        
        return "\n\n".join(context_parts) if context_parts else ""


# 全局RAG服务实例
_rag_service = None


def get_rag_service(collection_name: str = "academic_system_rag") -> RAGService:
    """
    获取RAG服务实例（单例模式）
    Args:
        collection_name: Milvus集合名称
    Returns:
        RAGService实例
    """
    global _rag_service
    if _rag_service is None:
        _rag_service = RAGService(collection_name)
    return _rag_service

