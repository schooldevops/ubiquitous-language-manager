CREATE TABLE term_master (
    term_id VARCHAR(20) PRIMARY KEY,
    term_no VARCHAR(30) NOT NULL UNIQUE,
    domain_name VARCHAR(100) NOT NULL,
    usage_type VARCHAR(50) NOT NULL,
    korean_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(300) NOT NULL,
    english_abbreviation VARCHAR(100) NOT NULL,
    business_definition TEXT NOT NULL,
    usage_context TEXT,
    physical_type VARCHAR(50) NOT NULL,
    digits INTEGER NOT NULL,
    decimal_point INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    owner VARCHAR(200) NOT NULL,
    version VARCHAR(30) NOT NULL DEFAULT '1.0',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_term_master_status CHECK (status IN ('Draft', 'Reviewing', 'Approved', 'Deprecated', 'Rejected')),
    CONSTRAINT ck_term_master_digits CHECK (digits >= 0),
    CONSTRAINT ck_term_master_decimal_point CHECK (decimal_point >= 0)
);

CREATE TABLE term_expression (
    expression_id BIGSERIAL PRIMARY KEY,
    term_id VARCHAR(20) NOT NULL REFERENCES term_master(term_id) ON DELETE CASCADE,
    expression_type VARCHAR(30) NOT NULL,
    expression_value VARCHAR(500) NOT NULL,
    language VARCHAR(20),
    style VARCHAR(50),
    is_standard BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_term_expression_type CHECK (
        expression_type IN ('Korean', 'English', 'DB_COLUMN', 'API_FIELD', 'CODE_VARIABLE', 'UI_LABEL', 'TEST_WORD')
    ),
    CONSTRAINT uq_term_expression_type_value UNIQUE (expression_type, expression_value)
);

CREATE TABLE term_alias (
    alias_id VARCHAR(20) PRIMARY KEY,
    term_id VARCHAR(20) NOT NULL REFERENCES term_master(term_id) ON DELETE CASCADE,
    alias_name VARCHAR(500) NOT NULL,
    alias_type VARCHAR(30) NOT NULL,
    recommendation_action VARCHAR(500) NOT NULL,
    reason TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_term_alias_type CHECK (alias_type IN ('Synonym', 'Forbidden', 'Deprecated', 'NeedsContext')),
    CONSTRAINT uq_term_alias_name UNIQUE (alias_name)
);

CREATE TABLE term_relationship (
    relationship_id BIGSERIAL PRIMARY KEY,
    source_term_id VARCHAR(20) NOT NULL REFERENCES term_master(term_id) ON DELETE CASCADE,
    relationship_type VARCHAR(30) NOT NULL,
    target_term_id VARCHAR(20) NOT NULL REFERENCES term_master(term_id) ON DELETE CASCADE,
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_term_relationship_type CHECK (
        relationship_type IN (
            'sameAs',
            'synonymOf',
            'relatedTo',
            'partOf',
            'broaderThan',
            'narrowerThan',
            'usedWith',
            'conflictsWith',
            'deprecatedBy'
        )
    ),
    CONSTRAINT ck_term_relationship_not_self CHECK (source_term_id <> target_term_id),
    CONSTRAINT uq_term_relationship UNIQUE (source_term_id, relationship_type, target_term_id)
);

CREATE TABLE term_change_history (
    change_history_id BIGSERIAL PRIMARY KEY,
    term_id VARCHAR(20) REFERENCES term_master(term_id) ON DELETE SET NULL,
    change_type VARCHAR(50) NOT NULL,
    previous_status VARCHAR(20),
    new_status VARCHAR(20),
    previous_value JSONB,
    new_value JSONB,
    reason TEXT NOT NULL,
    requested_by VARCHAR(100),
    approved_by VARCHAR(100),
    impact_analysis_id VARCHAR(50),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_term_change_history_previous_status CHECK (
        previous_status IS NULL OR previous_status IN ('Draft', 'Reviewing', 'Approved', 'Deprecated', 'Rejected')
    ),
    CONSTRAINT ck_term_change_history_new_status CHECK (
        new_status IS NULL OR new_status IN ('Draft', 'Reviewing', 'Approved', 'Deprecated', 'Rejected')
    )
);

CREATE TABLE term_candidate (
    candidate_id VARCHAR(20) PRIMARY KEY,
    requested_by VARCHAR(100) NOT NULL,
    requested_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    related_project VARCHAR(200),
    related_system VARCHAR(200),
    domain_name VARCHAR(100) NOT NULL,
    korean_name VARCHAR(200) NOT NULL,
    english_name VARCHAR(300),
    english_abbreviation VARCHAR(100),
    usage_type VARCHAR(50) NOT NULL,
    physical_type VARCHAR(50),
    digits INTEGER,
    decimal_point INTEGER,
    business_definition TEXT NOT NULL,
    usage_context TEXT,
    example_values TEXT,
    planned_db_column VARCHAR(100),
    planned_api_field VARCHAR(200),
    planned_code_variable VARCHAR(200),
    planned_ui_label VARCHAR(200),
    status VARCHAR(20) NOT NULL DEFAULT 'Draft',
    review_result TEXT,
    promoted_term_id VARCHAR(20) REFERENCES term_master(term_id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_term_candidate_status CHECK (status IN ('Draft', 'Reviewing', 'Approved', 'Rejected')),
    CONSTRAINT ck_term_candidate_digits CHECK (digits IS NULL OR digits >= 0),
    CONSTRAINT ck_term_candidate_decimal_point CHECK (decimal_point IS NULL OR decimal_point >= 0)
);

CREATE TABLE term_review_request (
    review_request_id VARCHAR(20) PRIMARY KEY,
    candidate_id VARCHAR(20) REFERENCES term_candidate(candidate_id) ON DELETE CASCADE,
    term_id VARCHAR(20) REFERENCES term_master(term_id) ON DELETE SET NULL,
    review_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Reviewing',
    requested_by VARCHAR(100) NOT NULL,
    reviewer VARCHAR(100),
    review_comment TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMPTZ,
    CONSTRAINT ck_term_review_request_status CHECK (status IN ('Draft', 'Reviewing', 'Approved', 'Rejected')),
    CONSTRAINT ck_term_review_request_target CHECK (candidate_id IS NOT NULL OR term_id IS NOT NULL)
);

CREATE INDEX ix_term_master_korean_name ON term_master (korean_name);
CREATE INDEX ix_term_master_english_name ON term_master (english_name);
CREATE INDEX ix_term_master_english_abbreviation ON term_master (english_abbreviation);
CREATE INDEX ix_term_master_domain_status ON term_master (domain_name, status);
CREATE INDEX ix_term_master_status ON term_master (status);

CREATE INDEX ix_term_expression_term_id ON term_expression (term_id);
CREATE INDEX ix_term_expression_type_value ON term_expression (expression_type, expression_value);
CREATE INDEX ix_term_expression_value ON term_expression (expression_value);

CREATE INDEX ix_term_alias_term_id ON term_alias (term_id);
CREATE INDEX ix_term_alias_name ON term_alias (alias_name);
CREATE INDEX ix_term_alias_type ON term_alias (alias_type);

CREATE INDEX ix_term_relationship_source ON term_relationship (source_term_id);
CREATE INDEX ix_term_relationship_target ON term_relationship (target_term_id);
CREATE INDEX ix_term_relationship_type ON term_relationship (relationship_type);

CREATE INDEX ix_term_change_history_term_id ON term_change_history (term_id);
CREATE INDEX ix_term_change_history_created_at ON term_change_history (created_at);

CREATE INDEX ix_term_candidate_domain_status ON term_candidate (domain_name, status);
CREATE INDEX ix_term_candidate_korean_name ON term_candidate (korean_name);

CREATE INDEX ix_term_review_request_candidate_id ON term_review_request (candidate_id);
CREATE INDEX ix_term_review_request_term_id ON term_review_request (term_id);
CREATE INDEX ix_term_review_request_status ON term_review_request (status);
