-- RAG 의미 검색용 term 임베딩 저장(pgvector). 차원 비고정(vector) 으로 모델 차원 변화에 견고.
CREATE TABLE term_embedding (
    term_id   VARCHAR(32) PRIMARY KEY REFERENCES term(term_id) ON DELETE CASCADE,
    embedding vector      NOT NULL
);
