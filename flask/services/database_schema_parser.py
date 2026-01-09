"""
数据库Schema解析器
从SQL文件中提取表结构、字段、关系等信息
"""
import re
import os
from pathlib import Path
from typing import List, Dict, Optional
from langchain_core.documents import Document


class DatabaseSchemaParser:
    """数据库Schema解析器"""
    
    def __init__(self, sql_dir: str = "database"):
        """
        初始化解析器
        Args:
            sql_dir: SQL文件目录路径
        """
        self.sql_dir = Path(sql_dir)
        if not self.sql_dir.exists():
            self.sql_dir.mkdir(parents=True, exist_ok=True)
    
    def parse_sql_file(self, sql_file_path: Path) -> List[Dict]:
        """
        解析单个SQL文件，提取表结构信息
        Args:
            sql_file_path: SQL文件路径
        Returns:
            表结构信息列表
        """
        try:
            content = sql_file_path.read_text(encoding='utf-8')
        except Exception as e:
            print(f"读取SQL文件失败 {sql_file_path}: {e}")
            return []
        
        tables = []
        
        # 匹配CREATE TABLE语句
        create_table_pattern = re.compile(
            r'CREATE\s+TABLE\s+(?:IF\s+NOT\s+EXISTS\s+)?`?(\w+)`?\s*\((.*?)\)\s*;?',
            re.IGNORECASE | re.DOTALL
        )
        
        matches = create_table_pattern.findall(content)
        
        for table_name, table_body in matches:
            table_info = self._parse_table_structure(table_name, table_body)
            if table_info:
                tables.append(table_info)
        
        return tables
    
    def _parse_table_structure(self, table_name: str, table_body: str) -> Optional[Dict]:
        """
        解析表结构
        Args:
            table_name: 表名
            table_body: 表体内容
        Returns:
            表结构字典
        """
        # 提取字段信息
        field_pattern = re.compile(
            r'`?(\w+)`?\s+(\w+(?:\(\d+(?:,\d+)?\))?)\s*(.*?)(?:,|$)',
            re.IGNORECASE | re.MULTILINE
        )
        
        fields = []
        primary_keys = []
        foreign_keys = []
        indexes = []
        
        # 解析字段
        field_matches = field_pattern.findall(table_body)
        for field_match in field_matches:
            field_name = field_match[0]
            field_type = field_match[1]
            field_attrs = field_match[2]
            
            field_info = {
                'name': field_name,
                'type': field_type,
                'nullable': 'NOT NULL' not in field_attrs.upper(),
                'default': None,
                'comment': None
            }
            
            # 提取默认值
            default_match = re.search(r'DEFAULT\s+([^,\s]+)', field_attrs, re.IGNORECASE)
            if default_match:
                field_info['default'] = default_match.group(1)
            
            # 提取注释
            comment_match = re.search(r"COMMENT\s+['\"](.*?)['\"]", field_attrs, re.IGNORECASE)
            if comment_match:
                field_info['comment'] = comment_match.group(1)
            
            # 检查主键
            if 'PRIMARY KEY' in field_attrs.upper() or 'AUTO_INCREMENT' in field_attrs.upper():
                primary_keys.append(field_name)
            
            fields.append(field_info)
        
        # 提取主键约束
        pk_pattern = re.compile(r'PRIMARY\s+KEY\s*\([^)]+\)', re.IGNORECASE)
        pk_matches = pk_pattern.findall(table_body)
        for pk_match in pk_matches:
            pk_fields = re.findall(r'`?(\w+)`?', pk_match)
            primary_keys.extend(pk_fields)
        
        # 提取外键约束
        fk_pattern = re.compile(
            r'FOREIGN\s+KEY\s*\([^)]+\)\s+REFERENCES\s+`?(\w+)`?\s*\([^)]+\)',
            re.IGNORECASE
        )
        fk_matches = fk_pattern.findall(table_body)
        for fk_match in fk_matches:
            fk_detail = re.search(
                r'FOREIGN\s+KEY\s*\(`?(\w+)`?\)\s+REFERENCES\s+`?(\w+)`?\s*\(`?(\w+)`?\)',
                table_body,
                re.IGNORECASE
            )
            if fk_detail:
                foreign_keys.append({
                    'field': fk_detail.group(1),
                    'references_table': fk_detail.group(2),
                    'references_field': fk_detail.group(3)
                })
        
        # 提取索引
        index_pattern = re.compile(
            r'(?:UNIQUE\s+)?(?:KEY|INDEX)\s+`?(\w+)`?\s*\([^)]+\)',
            re.IGNORECASE
        )
        index_matches = index_pattern.findall(table_body)
        indexes.extend(index_matches)
        
        # 提取表注释
        table_comment_match = re.search(
            r"COMMENT\s*=\s*['\"](.*?)['\"]",
            table_body,
            re.IGNORECASE
        )
        table_comment = table_comment_match.group(1) if table_comment_match else None
        
        return {
            'table_name': table_name,
            'fields': fields,
            'primary_keys': list(set(primary_keys)),
            'foreign_keys': foreign_keys,
            'indexes': indexes,
            'comment': table_comment
        }
    
    def parse_all_sql_files(self) -> List[Dict]:
        """
        解析目录下所有SQL文件
        Returns:
            所有表结构信息列表
        """
        all_tables = []
        
        # 查找所有SQL文件
        sql_files = list(self.sql_dir.glob("*.sql"))
        
        if not sql_files:
            print(f"警告: 在 {self.sql_dir} 目录下未找到SQL文件")
            return all_tables
        
        for sql_file in sql_files:
            print(f"正在解析SQL文件: {sql_file.name}")
            tables = self.parse_sql_file(sql_file)
            all_tables.extend(tables)
        
        return all_tables
    
    def format_table_info_for_rag(self, table_info: Dict) -> str:
        """
        将表结构信息格式化为RAG可用的文本
        Args:
            table_info: 表结构字典
        Returns:
            格式化后的文本
        """
        lines = []
        lines.append(f"表名: {table_info['table_name']}")
        
        if table_info.get('comment'):
            lines.append(f"表说明: {table_info['comment']}")
        
        lines.append("\n字段列表:")
        for field in table_info['fields']:
            field_desc = f"  - {field['name']} ({field['type']})"
            if not field['nullable']:
                field_desc += " NOT NULL"
            if field.get('default'):
                field_desc += f" DEFAULT {field['default']}"
            if field.get('comment'):
                field_desc += f" // {field['comment']}"
            lines.append(field_desc)
        
        if table_info['primary_keys']:
            lines.append(f"\n主键: {', '.join(table_info['primary_keys'])}")
        
        if table_info['foreign_keys']:
            lines.append("\n外键关系:")
            for fk in table_info['foreign_keys']:
                lines.append(
                    f"  - {fk['field']} -> {fk['references_table']}.{fk['references_field']}"
                )
        
        if table_info['indexes']:
            lines.append(f"\n索引: {', '.join(table_info['indexes'])}")
        
        return "\n".join(lines)
    
    def create_documents_for_rag(self) -> List[Document]:
        """
        创建用于RAG的Document列表
        Returns:
            Document列表
        """
        tables = self.parse_all_sql_files()
        documents = []
        
        for table_info in tables:
            # 为每个表创建多个角度的文档，提高检索效果
            table_name = table_info['table_name']
            
            # 1. 完整的表结构文档
            full_text = self.format_table_info_for_rag(table_info)
            documents.append(Document(
                page_content=full_text,
                metadata={
                    'type': 'table_schema',
                    'table_name': table_name,
                    'document_type': 'full_schema'
                }
            ))
            
            # 2. 字段列表文档（便于字段查询）
            fields_text = f"表 {table_name} 的字段:\n"
            for field in table_info['fields']:
                field_desc = f"- {field['name']} ({field['type']})"
                if field.get('comment'):
                    field_desc += f": {field['comment']}"
                fields_text += field_desc + "\n"
            
            documents.append(Document(
                page_content=fields_text,
                metadata={
                    'type': 'table_fields',
                    'table_name': table_name,
                    'document_type': 'fields_list'
                }
            ))
            
            # 3. 关系文档（便于关联查询）
            if table_info['foreign_keys']:
                relations_text = f"表 {table_name} 的关系:\n"
                for fk in table_info['foreign_keys']:
                    relations_text += f"- {fk['field']} 关联到 {fk['references_table']}.{fk['references_field']}\n"
                
                documents.append(Document(
                    page_content=relations_text,
                    metadata={
                        'type': 'table_relations',
                        'table_name': table_name,
                        'document_type': 'relations'
                    }
                ))
        
        return documents

