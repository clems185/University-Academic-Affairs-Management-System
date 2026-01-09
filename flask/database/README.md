# 数据库Schema文件目录

## 说明

此目录用于存放数据库的SQL文件。RAG系统会自动解析这些SQL文件，提取数据库表结构、字段、关系等信息，并将其向量化存储到Milvus中，用于智能问答。

## 使用方法

1. **放置SQL文件**
   - 将数据库的SQL文件（包含CREATE TABLE语句）放置在此目录下
   - 支持多个SQL文件
   - 文件扩展名必须是 `.sql`

2. **初始化RAG向量库**
   ```bash
   # 首次初始化（如果集合已存在，不会重建）
   python init_rag.py
   
   # 强制重建向量库（删除旧数据）
   python init_rag.py --rebuild
   ```

3. **SQL文件格式要求**
   - 必须包含CREATE TABLE语句
   - 建议包含字段注释（COMMENT）
   - 建议包含表注释
   - 支持外键、主键、索引等约束定义

## 示例SQL文件格式

```sql
CREATE TABLE `StudentInfo` (
  `StudentId` VARCHAR(20) NOT NULL COMMENT '学号',
  `StudentName` VARCHAR(50) NOT NULL COMMENT '姓名',
  `ClassName` VARCHAR(50) COMMENT '班级',
  `MajorName` VARCHAR(100) COMMENT '专业',
  PRIMARY KEY (`StudentId`)
) COMMENT='学生信息表';
```

## 注意事项

- SQL文件编码必须是UTF-8
- 确保Milvus服务已启动（默认地址：http://localhost:19530）
- 首次使用前需要运行初始化脚本
- 如果修改了SQL文件，需要重新运行初始化脚本（使用--rebuild参数）
