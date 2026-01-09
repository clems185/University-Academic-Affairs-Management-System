import os
from pathlib import Path

from langchain_core.retrievers import BaseRetriever
from langchain_milvus import Milvus
from pymilvus import connections, utility
from DoubaoEmbedding import VolcEngineEmbeddings

os.environ["ARK_API_KEY"] = "cdc4ee9b-9f38-474e-8e8a-dcba8c63c6b5"
embedding_model = VolcEngineEmbeddings(
    model_id="ep-20251203210744-bbfxp",
    api_key=os.environ["ARK_API_KEY"]
)

_vector_store = None


def connect_milvus():
    """
    连接到Milvus
    """
    try:
        connections.connect(
            alias="default",
            uri="http://localhost:19530",
            secure=False,
        )
        print("成功连接到Milvus")
    except Exception as e:
        print(f"连接到Milvus失败: {e}")
        raise e


def list_collections():
    """
    列出所有存在的集合
    """
    connect_milvus()  # 确保已连接
    try:
        collections = utility.list_collections()
        print(f"当前存在的集合: {collections}")
        return collections
    except Exception as e:
        print(f"获取集合列表失败: {e}")
        return []


def connect_existing_collection(collection_name="test_rag"):
    """
    连接到已有的Milvus集合

    Args:
        collection_name: 要连接的集合名称

    Returns:
        向量存储实例
    """
    global _vector_store
    try:
        # 检查集合是否存在
        connect_milvus()  # 确保已连接
        if not utility.has_collection(collection_name):
            print(f"集合 '{collection_name}' 不存在")
            print("可用的集合:")
            list_collections()
            raise ValueError(f"集合 '{collection_name}' 不存在")

        # 连接到已有集合
        _vector_store = Milvus(
            embedding_function=embedding_model,
            connection_args={
                "uri": "http://localhost:19530",
                "secure": False,
            },
            collection_name=collection_name,
            auto_id=True,
            drop_old=False,  # 重要：设置为False以保留现有数据
        )
        print(f"成功连接到已有集合 '{collection_name}'")

        # 可选：显示集合信息
        collection_info = utility.describe_collection(collection_name)
        print(f"集合信息: {collection_info}")

        # 获取向量数量
        num_entities = _vector_store.col.num_entities
        print(f"当前集合中的向量数量: {num_entities}")

        return _vector_store
    except Exception as e:
        print(f"连接已有集合失败: {e}")
        raise e


def init_vector_store(collection_name="test_rag", drop_old=False):
    """
    初始化Milvus向量存储

    Args:
        collection_name: 集合名称
        drop_old: 是否删除已存在的同名集合
    """
    global _vector_store
    try:
        _vector_store = Milvus(
            embedding_function=embedding_model,
            connection_args={
                "uri": "http://localhost:19530",
                "secure": False,
            },
            collection_name=collection_name,
            collection_description="RAG测试过程中的向量数据库",
            auto_id=True,
            drop_old=drop_old,
        )
        if drop_old:
            print(f"创建新集合 '{collection_name}'")
        else:
            print(f"初始化/连接到集合 '{collection_name}'")
        return _vector_store
    except Exception as e:
        print(f"创建Milvus向量存储失败: {e}")
        raise e


def get_vector_store(collection_name="test_rag", force_new=False):
    """
    获取向量存储实例

    Args:
        collection_name: 集合名称
        force_new: 是否强制创建新集合（删除已存在的）

    Returns:
        向量存储实例
    """
    global _vector_store
    if _vector_store is None or force_new:
        # 根据force_new参数决定是否删除旧集合
        _vector_store = init_vector_store(collection_name, drop_old=force_new)
    return _vector_store


def add_texts(texts, collection_name=None, metadatas=None):
    """
    将文本列表添加到向量存储中

    Args:
        texts: 文本列表
        collection_name: 可选，指定要添加到的集合名称
        metadatas: 可选，元数据列表，每个元素是一个字典
    """
    global _vector_store

    if collection_name:
        try:
            if _vector_store is None or _vector_store.collection_name != collection_name:
                if utility.has_collection(collection_name):
                    _vector_store = connect_existing_collection(collection_name)
                else:
                    _vector_store = init_vector_store(collection_name, drop_old=False)
        except:
            # 如果连接失败，创建新集合
            _vector_store = init_vector_store(collection_name, drop_old=False)

    if _vector_store is None:
        raise Exception("向量存储未初始化，请先调用init_vector_store或get_vector_store")

    try:
        print(f"正在添加文本到集合 '{_vector_store.collection_name}'...")

        # 检查 texts 中的元素是否是 Document 对象
        # 如果是，提取 page_content 和 metadata
        from langchain_core.documents import Document

        processed_texts = []
        processed_metadatas = []

        for item in texts:
            if isinstance(item, Document):
                processed_texts.append(item.page_content)
                if item.metadata:
                    processed_metadatas.append(item.metadata)
                else:
                    processed_metadatas.append({})
            else:
                processed_texts.append(item)
                if metadatas and len(metadatas) > len(processed_metadatas):
                    processed_metadatas.append(metadatas[len(processed_metadatas)])
                else:
                    processed_metadatas.append({})

        # 如果有元数据，并且格式正确，则传递
        if processed_metadatas and all(isinstance(m, dict) for m in processed_metadatas):
            _vector_store.add_texts(texts=processed_texts, metadatas=processed_metadatas)
        else:
            # 如果没有元数据或格式不正确，只添加文本
            _vector_store.add_texts(texts=processed_texts)

        print(f"成功添加 {len(processed_texts)} 个文本到Milvus")
    except Exception as e:
        print(f"添加文本失败: {e}")
        raise e

def search(query, k=1, collection_name=None, return_strings=True):
    """
    执行相似度搜索

    Args:
        query: 查询文本
        k: 返回结果数量
        collection_name: 可选，指定要搜索的集合名称
        return_strings: 是否返回字符串格式的结果（用于chain）
    """
    global _vector_store

    if _vector_store is None:
        raise Exception("向量存储未初始化，请先调用init_vector_store或get_vector_store")

    try:
        print(f"在集合 '{_vector_store.collection_name}' 中执行相似度搜索...")
        results = _vector_store.similarity_search_with_score(query=query, k=k)

        if return_strings:
            # 返回格式化后的字符串，便于RAG使用
            formatted_results = []
            for i, (doc, score) in enumerate(results):
                formatted_results.append(
                    f"文档{i + 1} (相似度: {score:.4f}): {doc.page_content[:200]}..."
                )
            return "\n\n".join(formatted_results)
        else:
            # 返回原始结果
            return results
    except Exception as e:
        print(f"搜索失败: {e}")
        raise e


def get_retriever(collection_name="test_rag"):
    """
    获取一个RAG可用的检索器
    """

    # 返回一个函数，该函数接收查询字符串并返回检索结果
    def retriever(query: str, k: int = 2):
        return search(query, k=k, collection_name=collection_name, return_strings=True)

    return retriever


def load_texts_from_directory(directory_path):
    """
    从指定目录加载所有txt文件，返回文本列表
    """
    path_list = list(Path(directory_path).glob("*.txt"))
    text_list = []
    for path in path_list:
        text = path.read_text(encoding="utf-8")
        text_list.append(text)
    return text_list


def get_lc_retriever(collection_name="test_rag", k: int = 4) -> BaseRetriever:
    """
    返回 LangChain 标准 retriever（返回 Document 列表）
    """
    global _vector_store
    if _vector_store is None or _vector_store.collection_name != collection_name:
        if utility.has_collection(collection_name):
            _vector_store = connect_existing_collection(collection_name)
        else:
            _vector_store = init_vector_store(collection_name, drop_old=False)

    return _vector_store.as_retriever(search_kwargs={"k": k})