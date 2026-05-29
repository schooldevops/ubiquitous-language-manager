# 데이터 사전 코어 DB 스키마

## 1. 목적

본 문서는 표준 용어 관리에 필요한 PostgreSQL 코어 스키마를 정의한다.

## 2. Migration 파일

| 파일 | 설명 |
|---|---|
| `apps/backend/src/main/resources/db/migration/V1__create_dictionary_core_schema.sql` | 코어 테이블, 제약조건, 인덱스 생성 |
| `apps/backend/src/main/resources/db/migration/V2__seed_dictionary_core_sample_data.sql` | MVP 샘플 용어 seed 데이터 |

## 3. 테이블 목록

| 테이블 | 설명 |
|---|---|
| `term_master` | 표준 용어의 중심 정보 |
| `term_expression` | DB 컬럼, API 필드, 코드 변수 등 표현 매핑 |
| `term_alias` | 유사어, 금지어, 폐기어, 문맥 확인 표현 |
| `term_relationship` | 용어 간 관계 |
| `term_change_history` | 용어 변경 이력 |
| `term_candidate` | 신규 용어 후보 |
| `term_review_request` | 용어 후보 또는 용어 변경 검토 요청 |

## 4. 주요 제약조건

| 테이블 | 제약조건 |
|---|---|
| `term_master` | `status`는 Draft, Reviewing, Approved, Deprecated, Rejected 중 하나 |
| `term_master` | `term_no`는 unique |
| `term_expression` | `expression_type`은 Korean, English, DB_COLUMN, API_FIELD, CODE_VARIABLE, UI_LABEL, TEST_WORD 중 하나 |
| `term_expression` | `expression_type`, `expression_value` 조합은 unique |
| `term_alias` | `alias_type`은 Synonym, Forbidden, Deprecated, NeedsContext 중 하나 |
| `term_alias` | `alias_name`은 unique |
| `term_relationship` | 자기 자신과 관계를 맺을 수 없음 |
| `term_relationship` | `source_term_id`, `relationship_type`, `target_term_id` 조합은 unique |
| `term_candidate` | 후보 상태는 Draft, Reviewing, Approved, Rejected 중 하나 |
| `term_review_request` | `candidate_id` 또는 `term_id` 중 하나는 반드시 존재 |

## 5. 검색 인덱스

| 인덱스 대상 | 목적 |
|---|---|
| `term_master.korean_name` | 한글 용어 정확 검색 |
| `term_master.english_name` | 영문명 정확 검색 |
| `term_master.english_abbreviation` | 약어 및 DB 컬럼 검색 |
| `term_master.domain_name, status` | 도메인/상태 필터 |
| `term_expression.expression_type, expression_value` | API 필드, 코드 변수, UI 라벨 검색 |
| `term_alias.alias_name` | 유사어, 금지어 검색 |
| `term_relationship.source_term_id` | 출발 용어 기준 관계 검색 |
| `term_relationship.target_term_id` | 대상 용어 기준 관계 검색 |

## 6. MVP seed 데이터

Seed 데이터는 다음 요구사항을 만족한다.

- 고객번호 샘플 용어를 저장한다.
- 고객번호의 DB 컬럼명 `CUST_NO`를 저장한다.
- 고객번호의 API 필드명 `customerNumber`를 저장한다.
- 고객번호의 코드 변수명 `customerNumber`를 저장한다.
- 고객ID를 고객번호의 유사어로 저장한다.
- CUST_ID를 고객번호의 금지어로 저장한다.

## 7. QueryDSL SQL 적용 기준

Phase 1 Backend scaffold 이후 다음 기준으로 QueryDSL SQL을 적용한다.

- Migration은 Flyway 또는 동등한 DB migration 도구로 실행한다.
- QueryDSL SQL metadata 생성 대상은 `term_*` 테이블이다.
- API 구현에서는 생성된 QueryDSL Q-type을 사용한다.
- 문자열 기반 SQL은 migration과 일부 bulk import를 제외하고 지양한다.

