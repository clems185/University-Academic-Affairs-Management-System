# RAG系统使用说明

## 概述

本系统为高校教务管理系统的Agent模块集成了RAG（检索增强生成）功能，使Agent能够基于数据库结构信息进行智能问答。

## 功能特性

1. **数据库Schema解析**：自动从SQL文件中提取表结构、字段、关系等信息
2. **向量化存储**：将schema信息向量化并存储到Milvus
3. **智能检索**：根据用户问题检索相关的数据库结构信息
4. **用户上下文**：自动识别用户身份（学生/教师/管理员）并提供上下文
5. **智能问答**：基于数据库结构和用户信息提供准确的回答

## 系统架构

```
┌─────────────────┐
│   Agent路由     │ (agentdash.py, agentmessage.py)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  AgentService   │ (agent_service.py)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   RAGService    │ (rag_service.py)
└────────┬────────┘
         │
    ┌────┴────┐
    ▼         ▼
┌─────────┐ ┌──────────┐
│ Milvus  │ │ Database │
│ 向量库  │ │ 用户信息 │
└─────────┘ └──────────┘
```

## 文件结构

```
flask/
├── database/              # SQL文件目录
│   ├── *.sql            # 数据库SQL文件
│   └── README.md        # SQL文件说明
├── services/
│   ├── agent_service.py          # Agent服务（已集成RAG）
│   ├── rag_service.py            # RAG服务核心
│   └── database_schema_parser.py # Schema解析器
├── utils/
│   └── jwt_utils.py              # JWT工具（用于获取用户ID）
├── init_rag.py                  # RAG初始化脚本
└── RAG_README.md                # 本文档
```

## 使用步骤

### 1. 准备SQL文件

将数据库的SQL文件（包含CREATE TABLE语句）放置到 `flask/database/` 目录下。

**要求：**
- 文件扩展名必须是 `.sql`
- 编码必须是 UTF-8
- 必须包含CREATE TABLE语句
- 建议包含字段注释和表注释

### 2. 初始化RAG向量库

首次使用前，需要运行初始化脚本：

```bash
cd flask
python init_rag.py
```

如果需要重建向量库（删除旧数据）：

```bash
python init_rag.py --rebuild
```

### 3. 启动服务

确保以下服务已启动：

- **Milvus服务**：默认地址 `http://localhost:19530`
- **Flask应用**：运行 `python app.py` 或 `python run.py`

### 4. 使用Agent

Agent会自动使用RAG功能：

- **Dashboard Agent** (`/api/agentdash/chat`)：已集成RAG，会自动检索数据库结构信息
- **Message Agent** (`/api/agentmessage/chat`)：暂未集成RAG（主要用于消息审核）

## API使用示例

### Dashboard Agent（已集成RAG）

```python
POST /api/agentdash/chat
Content-Type: application/json
Authorization: Bearer <token>

{
  "question": "我的成绩怎么样？",
  "userId": "2021001",  // 可选，如果不提供会从token中解析
  "conversationId": "xxx"  // 可选
}
```

**响应：**
```json
{
  "IsSuccess": true,
  "Message": "成功",
  "Result": {
    "answer": "根据您的学号，我可以帮您查询成绩信息...",
    "conversationId": "xxx"
  }
}
```

## 工作原理

1. **用户提问** → Agent接收问题
2. **RAG检索** → 根据问题检索相关的数据库schema信息
3. **用户识别** → 从token或请求参数中获取用户ID，查询用户信息
4. **上下文构建** → 将schema信息和用户信息组合成上下文
5. **LLM生成** → 基于上下文和问题生成回答

## 配置说明

### Milvus配置

在 `milvus_store.py` 中配置Milvus连接：

```python
connections.connect(
    alias="default",
    uri="http://localhost:19530",  # Milvus地址
    secure=False,
)
```

### Embedding模型配置

在 `milvus_store.py` 中配置Embedding模型：

```python
os.environ["ARK_API_KEY"] = "your-api-key"
embedding_model = VolcEngineEmbeddings(
    model_id="ep-20251203210744-bbfxp",
    api_key=os.environ["ARK_API_KEY"]
)
```

### 数据库配置

在 `config.py` 中配置数据库连接信息。

## 用户信息查询

系统会自动尝试查询以下表来获取用户信息：

1. `StudentInfo` - 学生信息
2. `TeacherInfo` - 教师信息
3. `AdminInfo` - 管理员信息

如果您的数据库表名不同，可以修改 `rag_service.py` 中的 `get_user_context` 方法。

## 故障排查

### 1. RAG检索失败

- 检查Milvus服务是否运行
- 检查是否已运行初始化脚本
- 查看日志中的错误信息

### 2. 用户信息查询失败

- 检查数据库连接配置
- 检查表名是否正确
- 检查用户ID是否正确传递

### 3. Schema解析失败

- 检查SQL文件格式是否正确
- 检查文件编码是否为UTF-8
- 查看初始化脚本的错误信息

## 注意事项

1. **首次使用**：必须先运行 `init_rag.py` 初始化向量库
2. **更新SQL文件**：修改SQL文件后，需要重新运行初始化脚本（使用 `--rebuild` 参数）
3. **性能优化**：大量SQL文件可能导致初始化时间较长
4. **用户隐私**：确保用户ID的传递符合隐私保护要求

## 扩展开发

### 添加新的用户表

在 `rag_service.py` 的 `get_user_context` 方法中添加新的表配置：

```python
{
    'table': 'YourTableName',
    'id_field': 'YourIdField',
    'name_field': 'YourNameField',
    'role': '角色名称',
    'id_label': 'ID标签',
    'extra_fields': ['Field1', 'Field2'],
    'extra_labels': ['标签1', '标签2']
}
```

### 自定义检索策略

修改 `rag_service.py` 中的 `retrieve_relevant_schema` 方法，调整检索参数：

```python
# 调整检索数量
rag_context = self.rag_service.build_rag_context(
    query=question,
    user_id=user_id,
    k=5  # 增加检索数量
)
```

## 技术支持

如有问题，请检查：
1. 日志输出
2. Milvus服务状态
3. 数据库连接状态
4. SQL文件格式

