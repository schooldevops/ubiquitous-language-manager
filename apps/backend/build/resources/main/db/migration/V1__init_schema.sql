-- AULMS postgres 스키마 (영속화 postgres 프로파일)
-- 모든 enum 은 문자열로 저장한다(DTO enum name == value).

-- pgvector 확장 (docker init 에서도 생성하나 멱등 보장)
CREATE EXTENSION IF NOT EXISTS vector;

-- ID 시퀀스. memory 모드 seed 와 동일하게 새 생성분 시작값을 맞춘다.
CREATE SEQUENCE IF NOT EXISTS term_seq START 18;
CREATE SEQUENCE IF NOT EXISTS term_expression_seq START 36;
CREATE SEQUENCE IF NOT EXISTS term_alias_seq START 13;
CREATE SEQUENCE IF NOT EXISTS term_change_history_seq START 1;
CREATE SEQUENCE IF NOT EXISTS candidate_seq START 1;
CREATE SEQUENCE IF NOT EXISTS candidate_history_seq START 1;

-- 표준 용어 -----------------------------------------------------------------
CREATE TABLE term (
    term_id              VARCHAR(32)  PRIMARY KEY,
    term_number          VARCHAR(32)  NOT NULL,
    domain_name          VARCHAR(100) NOT NULL,
    usage_type           VARCHAR(50)  NOT NULL,
    korean_name          VARCHAR(200) NOT NULL,
    english_name         VARCHAR(300) NOT NULL,
    english_abbreviation VARCHAR(200) NOT NULL,
    business_definition  TEXT         NOT NULL,
    physical_type        VARCHAR(50)  NOT NULL,
    digits               INT          NOT NULL,
    decimal_point        INT          NOT NULL,
    status               VARCHAR(20)  NOT NULL,
    owner                VARCHAR(100) NOT NULL,
    version              VARCHAR(20)  NOT NULL,
    usage_context        TEXT,
    created_at           TIMESTAMPTZ,
    updated_at           TIMESTAMPTZ
);

CREATE TABLE term_expression (
    expression_id    BIGINT       PRIMARY KEY,
    term_id          VARCHAR(32)  NOT NULL REFERENCES term(term_id) ON DELETE CASCADE,
    expression_type  VARCHAR(20)  NOT NULL,
    expression_value VARCHAR(500) NOT NULL,
    is_standard      BOOLEAN      NOT NULL,
    language         VARCHAR(20),
    style            VARCHAR(50)
);
CREATE INDEX idx_term_expression_term ON term_expression(term_id);

CREATE TABLE term_alias (
    alias_id              VARCHAR(32)  PRIMARY KEY,
    term_id               VARCHAR(32)  NOT NULL REFERENCES term(term_id) ON DELETE CASCADE,
    alias_name            VARCHAR(200) NOT NULL,
    alias_type            VARCHAR(20)  NOT NULL,
    recommendation_action VARCHAR(300) NOT NULL,
    reason                TEXT         NOT NULL
);
CREATE INDEX idx_term_alias_term ON term_alias(term_id);

CREATE TABLE term_relationship (
    id                BIGSERIAL    PRIMARY KEY,
    source_term_id    VARCHAR(32)  NOT NULL,
    relationship_type VARCHAR(30)  NOT NULL,
    target_term_id    VARCHAR(32)  NOT NULL,
    description       TEXT         NOT NULL
);

CREATE TABLE term_change_history (
    change_history_id BIGINT       PRIMARY KEY,
    term_id           VARCHAR(32),
    change_type       VARCHAR(30)  NOT NULL,
    reason            TEXT         NOT NULL,
    previous_status   VARCHAR(20),
    new_status        VARCHAR(20),
    requested_by      VARCHAR(100),
    approved_by       VARCHAR(100),
    impact_analysis_id VARCHAR(32),
    created_at        TIMESTAMPTZ  NOT NULL
);
CREATE INDEX idx_term_history_term ON term_change_history(term_id);

-- 신규 용어 후보 -------------------------------------------------------------
CREATE TABLE term_candidate (
    candidate_id         VARCHAR(32)  PRIMARY KEY,
    korean_name          VARCHAR(200) NOT NULL,
    english_name         VARCHAR(300) NOT NULL,
    english_abbreviation VARCHAR(200) NOT NULL,
    domain_name          VARCHAR(100) NOT NULL,
    business_definition  TEXT         NOT NULL,
    physical_type        VARCHAR(50)  NOT NULL,
    digits               INT          NOT NULL,
    decimal_point        INT          NOT NULL,
    status               VARCHAR(20)  NOT NULL,
    requested_by         VARCHAR(100) NOT NULL,
    usage_context        TEXT,
    reviewed_by          VARCHAR(100),
    promoted_term_id     VARCHAR(32),
    created_at           TIMESTAMPTZ,
    updated_at           TIMESTAMPTZ
);

CREATE TABLE candidate_similar_term (
    id           BIGSERIAL    PRIMARY KEY,
    candidate_id VARCHAR(32)  NOT NULL REFERENCES term_candidate(candidate_id) ON DELETE CASCADE,
    term_id      VARCHAR(32)  NOT NULL,
    korean_name  VARCHAR(200) NOT NULL,
    english_name VARCHAR(300) NOT NULL,
    db_column    VARCHAR(200) NOT NULL,
    api_field    VARCHAR(200) NOT NULL,
    reason       TEXT         NOT NULL
);
CREATE INDEX idx_candidate_similar_candidate ON candidate_similar_term(candidate_id);

CREATE TABLE candidate_history (
    history_id   VARCHAR(40)  PRIMARY KEY,
    candidate_id VARCHAR(32)  NOT NULL REFERENCES term_candidate(candidate_id) ON DELETE CASCADE,
    status       VARCHAR(20)  NOT NULL,
    reason       TEXT         NOT NULL,
    actor        VARCHAR(100) NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL
);
CREATE INDEX idx_candidate_history_candidate ON candidate_history(candidate_id);

-- 프롬프트 템플릿 ------------------------------------------------------------
CREATE TABLE prompt_template (
    template_id    VARCHAR(40)  PRIMARY KEY,
    type           VARCHAR(30)  NOT NULL,
    name           VARCHAR(200) NOT NULL,
    version        VARCHAR(20)  NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    description    TEXT         NOT NULL,
    body           TEXT         NOT NULL,
    version_policy TEXT         NOT NULL,
    created_at     TIMESTAMPTZ  NOT NULL,
    updated_at     TIMESTAMPTZ  NOT NULL
);

CREATE TABLE prompt_template_variable (
    id          BIGSERIAL    PRIMARY KEY,
    template_id VARCHAR(40)  NOT NULL REFERENCES prompt_template(template_id) ON DELETE CASCADE,
    name        VARCHAR(100) NOT NULL,
    description TEXT         NOT NULL,
    required    BOOLEAN      NOT NULL,
    source      VARCHAR(30)  NOT NULL,
    ordinal     INT          NOT NULL
);
CREATE INDEX idx_prompt_variable_template ON prompt_template_variable(template_id);

CREATE TABLE prompt_template_history (
    history_id  VARCHAR(40)  PRIMARY KEY,
    template_id VARCHAR(40)  NOT NULL REFERENCES prompt_template(template_id) ON DELETE CASCADE,
    version     VARCHAR(20)  NOT NULL,
    change_type VARCHAR(20)  NOT NULL,
    reason      TEXT         NOT NULL,
    actor       VARCHAR(100) NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL
);
CREATE INDEX idx_prompt_history_template ON prompt_template_history(template_id);

CREATE TABLE prompt_template_version (
    id            BIGSERIAL    PRIMARY KEY,
    template_id   VARCHAR(40)  NOT NULL REFERENCES prompt_template(template_id) ON DELETE CASCADE,
    version       VARCHAR(20)  NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    change_reason TEXT         NOT NULL,
    created_by    VARCHAR(100) NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL
);
CREATE INDEX idx_prompt_version_template ON prompt_template_version(template_id);
