-- pgvector 확장 활성화 (RAG 의미 검색용 벡터 저장)
CREATE EXTENSION IF NOT EXISTS vector;

-- 애플리케이션 스키마/테이블은 backend 마이그레이션(Flyway 등)에 위임.
-- 여기서는 확장 활성화만 수행한다.
