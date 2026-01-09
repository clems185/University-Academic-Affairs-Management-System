from typing import List

import requests
from langchain_core.embeddings import Embeddings


class VolcEngineEmbeddings(Embeddings):
    """火山引擎豆包Embedding模型封装"""

    def __init__(self, model_id: str, api_key: str):
        self.model_id = model_id
        self.api_key = api_key
        self.base_url = "https://ark.cn-beijing.volces.com/api/v3"
        self.headers = {
            "Content-Type": "application/json",
        }

    def _call_api(self, text: str) -> List[float]:
        """调用火山引擎API获取单个文本的嵌入"""
        url = f"{self.base_url}/embeddings"
        payload = {
            "model": self.model_id,
            "input": text,
            "encoding_format": "float"
        }

        # 注意：火山引擎API使用Bearer token认证
        headers_with_auth = self.headers.copy()
        headers_with_auth["Authorization"] = f"Bearer {self.api_key}"

        try:
            response = requests.post(
                url,
                headers=headers_with_auth,
                json=payload,
                timeout=60
            )
            response.raise_for_status()
            result = response.json()

            if "data" in result and len(result["data"]) > 0:
                return result["data"][0]["embedding"]
            else:
                print(f"API响应格式异常: {result}")
                # 返回零向量作为fallback
                return [0.0] * 1024  # 假设是1024维，根据实际调整

        except requests.exceptions.RequestException as e:
            print(f"火山引擎API请求失败: {e}")
            if hasattr(e, 'response'):
                print(f"响应内容: {e.response.text}")
            return [0.0] * 1024

    def embed_documents(self, texts: List[str]) -> List[List[float]]:
        """批量生成文档嵌入"""
        embeddings = []
        for i, text in enumerate(texts):
            print(f"正在处理第 {i + 1}/{len(texts)} 个文本...")
            embedding = self._call_api(text)
            embeddings.append(embedding)
        return embeddings

    def embed_query(self, text: str) -> List[float]:
        """生成查询嵌入"""
        return self._call_api(text)
