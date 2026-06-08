-- 용어 JSONL 업로드 결과 추적 (postgres 전용)
CREATE SEQUENCE IF NOT EXISTS term_upload_batch_seq START 1;

CREATE TABLE term_upload_row (
    upload_row_id   BIGSERIAL    PRIMARY KEY,
    upload_batch_id VARCHAR(40)  NOT NULL,
    line_no         INT          NOT NULL,
    term_id         VARCHAR(32),
    status          VARCHAR(20)  NOT NULL,
    raw_json        TEXT         NOT NULL,
    error_message   TEXT,
    registered_at   TIMESTAMPTZ,
    failed_at       TIMESTAMPTZ,
    uploaded_at     TIMESTAMPTZ  NOT NULL
);
CREATE INDEX idx_term_upload_batch ON term_upload_row(upload_batch_id);
CREATE INDEX idx_term_upload_batch_status ON term_upload_row(upload_batch_id, status);
