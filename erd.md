# 유비쿼터스 랭기지 저장 모델 종합 장표

## 1. 목적

본 문서는 지금까지 정의한 데이터 사전 기반 유비쿼터스 랭기지 저장 테이블의 ERD, 적재 순서, 운영 흐름, AI/RAG/Graphify 활용 방안을 하나로 정리한다.

핵심 목표는 다음과 같다.

- 기획 용어, DB 컬럼, API 필드, 코드 변수, 테스트 용어를 하나의 표준 의미 체계로 연결한다.
- 승인된 표준 용어만 개발 산출물에 사용한다.
- 신규 용어는 후보, 검토, 승인, 승격 흐름으로 관리한다.
- AI와 바이브코딩은 데이터 사전, RAG, Graphify를 참조해 임의 명명 생성을 줄인다.

## 2. 테이블 그룹

| 그룹 | 테이블 | 역할 |
|---|---|---|
| 표준 용어 | `term_master` | 표준 용어의 중심 정보 |
| 표현 매핑 | `term_expression` | 한글명, 영문명, DB 컬럼, API 필드, 코드 변수, UI 라벨, 테스트 용어 매핑 |
| 별칭/금지어 | `term_alias` | 유사어, 금지어, 폐기어, 문맥 확인 표현 |
| 용어 관계 | `term_relationship` | 용어 간 `usedWith`, `relatedTo`, `deprecatedBy` 등 관계 |
| 변경 이력 | `term_change_history` | 용어 등록, 수정, 승인, 폐기 이력 |
| 후보 관리 | `term_candidate` | 신규 용어 후보 |
| 검토 관리 | `term_review_request` | 후보 또는 용어 변경 검토 |
| 프롬프트 관리 | `prompt_template`, `prompt_template_version`, `prompt_template_history` | AI/바이브코딩 프롬프트 템플릿 관리 권장 테이블 |
| Graphify 동기화 | `graph_sync_log` | 그래프 동기화 실행 로그 권장 테이블 |

## 3. Core ERD

```mermaid
erDiagram
    TERM_MASTER_표준용어마스터 {
        varchar term_id PK "용어ID"
        varchar term_no UK "용어번호"
        varchar domain_name "도메인명"
        varchar usage_type "사용구분"
        varchar korean_name "한글용어명"
        varchar english_name "영문용어명"
        varchar english_abbreviation "영문약어"
        text business_definition "업무정의"
        text usage_context "사용맥락"
        varchar physical_type "물리타입"
        integer digits "자릿수"
        integer decimal_point "소수점자리수"
        varchar status "상태"
        varchar owner "소유자"
        varchar version "버전"
        timestamptz created_at "생성일시"
        timestamptz updated_at "수정일시"
    }

    TERM_EXPRESSION_용어표현매핑 {
        bigint expression_id PK "표현ID"
        varchar term_id FK "용어ID"
        varchar expression_type "표현유형"
        varchar expression_value "표현값"
        varchar language "언어"
        varchar style "표기스타일"
        boolean is_standard "표준여부"
        timestamptz created_at "생성일시"
        timestamptz updated_at "수정일시"
    }

    TERM_ALIAS_용어별칭 {
        varchar alias_id PK "별칭ID"
        varchar term_id FK "용어ID"
        varchar alias_name UK "별칭명"
        varchar alias_type "별칭유형"
        varchar recommendation_action "권장조치"
        text reason "사유"
        timestamptz created_at "생성일시"
        timestamptz updated_at "수정일시"
    }

    TERM_RELATIONSHIP_용어관계 {
        bigint relationship_id PK "관계ID"
        varchar source_term_id FK "출발용어ID"
        varchar relationship_type "관계유형"
        varchar target_term_id FK "대상용어ID"
        text description "관계설명"
        timestamptz created_at "생성일시"
        timestamptz updated_at "수정일시"
    }

    TERM_CHANGE_HISTORY_용어변경이력 {
        bigint change_history_id PK "변경이력ID"
        varchar term_id FK "용어ID"
        varchar change_type "변경유형"
        varchar previous_status "이전상태"
        varchar new_status "신규상태"
        jsonb previous_value "이전값"
        jsonb new_value "신규값"
        text reason "변경사유"
        varchar requested_by "요청자"
        varchar approved_by "승인자"
        varchar impact_analysis_id "영향도분석ID"
        timestamptz created_at "생성일시"
    }

    TERM_CANDIDATE_신규용어후보 {
        varchar candidate_id PK "후보ID"
        varchar requested_by "요청자"
        timestamptz requested_at "요청일시"
        varchar related_project "관련프로젝트"
        varchar related_system "관련시스템"
        varchar domain_name "도메인명"
        varchar korean_name "한글용어명"
        varchar english_name "영문용어명"
        varchar english_abbreviation "영문약어"
        varchar usage_type "사용구분"
        varchar physical_type "물리타입"
        integer digits "자릿수"
        integer decimal_point "소수점자리수"
        text business_definition "업무정의"
        text usage_context "사용맥락"
        text example_values "예시값"
        varchar planned_db_column "예정DB컬럼명"
        varchar planned_api_field "예정API필드명"
        varchar planned_code_variable "예정코드변수명"
        varchar planned_ui_label "예정화면라벨"
        varchar status "상태"
        text review_result "검토결과"
        varchar promoted_term_id FK "승격용어ID"
        timestamptz created_at "생성일시"
        timestamptz updated_at "수정일시"
    }

    TERM_REVIEW_REQUEST_용어검토요청 {
        varchar review_request_id PK "검토요청ID"
        varchar candidate_id FK "후보ID"
        varchar term_id FK "용어ID"
        varchar review_type "검토유형"
        varchar status "상태"
        varchar requested_by "요청자"
        varchar reviewer "검토자"
        text review_comment "검토의견"
        timestamptz created_at "생성일시"
        timestamptz reviewed_at "검토일시"
    }

    TERM_MASTER_표준용어마스터 ||--o{ TERM_EXPRESSION_용어표현매핑 : has
    TERM_MASTER_표준용어마스터 ||--o{ TERM_ALIAS_용어별칭 : has
    TERM_MASTER_표준용어마스터 ||--o{ TERM_RELATIONSHIP_용어관계 : source
    TERM_MASTER_표준용어마스터 ||--o{ TERM_RELATIONSHIP_용어관계 : target
    TERM_MASTER_표준용어마스터 ||--o{ TERM_CHANGE_HISTORY_용어변경이력 : changed
    TERM_MASTER_표준용어마스터 ||--o{ TERM_CANDIDATE_신규용어후보 : promoted
    TERM_MASTER_표준용어마스터 ||--o{ TERM_REVIEW_REQUEST_용어검토요청 : reviewed
    TERM_CANDIDATE_신규용어후보 ||--o{ TERM_REVIEW_REQUEST_용어검토요청 : reviewed
```

## 4. 권장 확장 ERD

프롬프트 템플릿과 Graphify 동기화 로그는 MVP3/MVP4에서 인메모리로 구현되어 있으나, 운영 DB에서는 아래 구조로 분리하는 것을 권장한다.

```mermaid
erDiagram
    PROMPT_TEMPLATE_프롬프트템플릿 {
        varchar template_id PK "템플릿ID"
        varchar template_type "템플릿유형"
        varchar name "템플릿명"
        varchar version "버전"
        varchar status "상태"
        text description "설명"
        text body "본문"
        text version_policy "버전정책"
        timestamptz created_at "생성일시"
        timestamptz updated_at "수정일시"
    }

    PROMPT_TEMPLATE_VARIABLE_프롬프트변수 {
        bigint variable_id PK "변수ID"
        varchar template_id FK "템플릿ID"
        varchar variable_name "변수명"
        text description "설명"
        boolean required "필수여부"
        varchar source "출처"
        timestamptz created_at "생성일시"
    }

    PROMPT_TEMPLATE_VERSION_프롬프트버전 {
        bigint version_id PK "버전ID"
        varchar template_id FK "템플릿ID"
        varchar version "버전"
        varchar status "상태"
        text body "본문"
        text change_reason "변경사유"
        varchar created_by "생성자"
        timestamptz created_at "생성일시"
    }

    PROMPT_TEMPLATE_HISTORY_프롬프트변경이력 {
        bigint history_id PK "이력ID"
        varchar template_id FK "템플릿ID"
        varchar version "버전"
        varchar change_type "변경유형"
        text reason "사유"
        varchar actor "행위자"
        timestamptz created_at "생성일시"
    }

    GRAPH_SYNC_LOG_그래프동기화로그 {
        bigint log_id PK "로그ID"
        varchar mode "동기화모드"
        boolean success "성공여부"
        integer created_nodes "생성노드수"
        integer updated_nodes "수정노드수"
        integer created_edges "생성엣지수"
        integer updated_edges "수정엣지수"
        integer skipped_items "건너뜀항목수"
        integer retry_count "재시도수"
        text message "메시지"
        timestamptz created_at "생성일시"
    }

    PROMPT_TEMPLATE_프롬프트템플릿 ||--o{ PROMPT_TEMPLATE_VARIABLE_프롬프트변수 : has
    PROMPT_TEMPLATE_프롬프트템플릿 ||--o{ PROMPT_TEMPLATE_VERSION_프롬프트버전 : versions
    PROMPT_TEMPLATE_프롬프트템플릿 ||--o{ PROMPT_TEMPLATE_HISTORY_프롬프트변경이력 : changed
```

## 5. 적재 순서

```mermaid
flowchart TD
    A["1. 원천 데이터 사전 수집"] --> B["2. 도메인/사용구분 정리"]
    B --> C["3. TERM_MASTER 적재"]
    C --> D["4. TERM_EXPRESSION 적재"]
    C --> E["5. TERM_ALIAS 적재"]
    C --> F["6. TERM_RELATIONSHIP 적재"]
    C --> G["7. TERM_CHANGE_HISTORY 초기 이력 적재"]
    D --> H["8. 정확 검색 인덱스 구성"]
    E --> I["9. 유사어/금지어 검색 인덱스 구성"]
    F --> J["10. Graphify 관계 동기화"]
    C --> K["11. RAG 용어 문서 생성"]
    H --> L["12. API/문서/코드 검증 기능 사용"]
    I --> L
    J --> M["13. 영향도 분석"]
    K --> N["14. AI 질의응답/바이브코딩"]
```

## 6. 신규 용어 승인 적재 흐름

```mermaid
sequenceDiagram
    participant Planner as 기획자
    participant AI as AI 용어 검토
    participant Candidate as TERM_CANDIDATE
    participant Review as TERM_REVIEW_REQUEST
    participant Master as TERM_MASTER
    participant Expr as TERM_EXPRESSION
    participant Graph as Graphify
    participant RAG as RAG Index

    Planner->>AI: 요구사항/기획서 초안 입력
    AI->>Master: 기존 표준 용어 검색
    AI->>Candidate: 신규 후보 등록
    Candidate->>Review: 검토 요청 생성
    Review->>Review: 데이터 스튜어드/아키텍트 검토
    Review->>Master: 승인 후보를 표준 용어로 승격
    Master->>Expr: DB/API/코드/UI/테스트 표현 적재
    Master->>Graph: 그래프 동기화
    Master->>RAG: 용어 문서 재생성/임베딩
```

## 7. 상태 전이

```mermaid
stateDiagram-v2
    [*] --> Draft
    Draft --> Reviewing: 검토 요청
    Reviewing --> Approved: 승인
    Reviewing --> Rejected: 반려
    Approved --> Deprecated: 폐기 승인
    Deprecated --> [*]
    Rejected --> [*]

    note right of Draft
      기획서 후보 사용 가능
      개발 산출물 사용 불가
    end note

    note right of Approved
      기획서/API/DB/코드/테스트 사용 가능
    end note

    note right of Deprecated
      신규 사용 금지
      대체 용어 안내
    end note
```

## 8. 핵심 테이블 활용 방안

| 활용 영역 | 사용하는 테이블 | 방식 |
|---|---|---|
| 정확 검색 | `term_master`, `term_expression` | 한글명, 영문명, DB 컬럼, API 필드, 코드 변수 exact match |
| 유사어 검색 | `term_alias`, `term_master` | 고객ID, customerId, CUST_ID를 고객번호로 변환 또는 금지 경고 |
| 의미 검색/RAG | `term_master`, `term_expression`, `term_alias` | 업무 정의, 사용 맥락, 예시 문장을 문서화해 임베딩 검색 |
| 관계 검색 | `term_relationship` | 관련 용어, 함께 쓰는 용어, 대체 용어 탐색 |
| 기획서 검토 | `term_master`, `term_alias`, `term_candidate` | 비표준 표현 탐지, 표준 용어 추천, 신규 후보 분리 |
| 바이브코딩 | `term_master`, `term_expression`, `prompt_template` | DTO/API/SQL 생성 시 표준 표현 주입 |
| PR/CI 검증 | `term_expression`, `term_alias`, `term_change_history` | DDL, OpenAPI, 코드 변수, 테스트 용어 검증 |
| 영향도 분석 | `term_relationship`, Graphify graph | 용어 변경 시 DB/API/DTO/문서/테스트 영향 추적 |
| 운영 감사 | `term_change_history`, `term_review_request`, `graph_sync_log` | 승인/폐기/동기화 이력 추적 |

## 9. AI/RAG/Graphify 활용 구조

```mermaid
flowchart LR
    Req["요구사항/기획서"] --> Extract["업무 개념 추출"]
    Extract --> Exact["정확 검색<br/>term_master, term_expression"]
    Exact --> Alias["유사어 검색<br/>term_alias"]
    Alias --> Semantic["의미 검색/RAG<br/>업무정의, 사용맥락"]
    Semantic --> Graph["Graphify 관계 확장<br/>term_relationship + 산출물 graph"]
    Graph --> Rule["Rule Engine 검증"]
    Rule --> Output["API/DB/DTO/테스트/문서 생성"]
    Rule --> Candidate["신규 용어 후보<br/>term_candidate"]
    Candidate --> Review["검토/승인<br/>term_review_request"]
    Review --> Master["표준 반영<br/>term_master"]
```

## 10. Graphify 동기화 구조

```mermaid
flowchart TD
    TM["term_master"] --> T["Term Node"]
    TM --> D["Domain Node"]
    TE["term_expression"] --> C["Column Node"]
    TE --> AF["APIField Node"]
    TE --> DF["DTOField Node"]
    TA["term_alias"] --> AL["Alias Node"]
    TR["term_relationship"] --> REL["Term-Term Edge"]

    T --> BD["belongsTo Domain"]
    C --> RT["represents Term"]
    AF --> RT
    DF --> RT
    AL --> SYN["synonym/forbidden/deprecatedBy"]
    REL --> IMP["영향도 분석"]
    RT --> IMP
    SYN --> IMP
```

## 11. 고객번호 예시

```mermaid
graph LR
    Term["TERM_MASTER<br/>고객번호<br/>T-000001"]
    DbExpr["TERM_EXPRESSION<br/>DB_COLUMN=CUST_NO"]
    ApiExpr["TERM_EXPRESSION<br/>API_FIELD=customerNumber"]
    CodeExpr["TERM_EXPRESSION<br/>CODE_VARIABLE=customerNumber"]
    Alias1["TERM_ALIAS<br/>고객ID<br/>Synonym"]
    Alias2["TERM_ALIAS<br/>CUST_ID<br/>Forbidden"]
    Api["OpenAPI<br/>GET /orders"]
    Dto["DTO<br/>CustomerOrderResponse.customerNumber"]
    Test["TestCase<br/>고객번호 필수 검증"]

    DbExpr -->|represents| Term
    ApiExpr -->|represents| Term
    CodeExpr -->|represents| Term
    Alias1 -->|synonymOf| Term
    Alias2 -->|forbidden; use CUST_NO| Term
    Api -->|uses| ApiExpr
    Dto -->|uses| CodeExpr
    Test -->|mentions| Term
```

## 12. 데이터 품질 규칙

| 규칙 | 설명 |
|---|---|
| 표준 용어 중복 금지 | `korean_name`, `english_abbreviation` 중복 검토 |
| 표현값 중복 금지 | `expression_type`, `expression_value` 조합 unique |
| 별칭 중복 금지 | `alias_name` unique |
| 자기 관계 금지 | `source_term_id <> target_term_id` |
| Approved만 개발 사용 | Draft/Reviewing은 개발 산출물 사용 불가 |
| Deprecated 사용 금지 | 대체 용어를 `deprecatedBy` 또는 별칭 reason으로 안내 |
| 물리 스펙 검증 | `physical_type`, `digits`, `decimal_point` 불일치 검출 |
| 변경 이력 필수 | 승인, 폐기, 주요 수정은 `term_change_history` 기록 |

## 13. 운영 활용 시나리오

### 13.1 기획서 표준화

```text
입력: 고객 ID를 입력하면 주문 리스트를 조회한다.
검색: term_alias.고객ID -> term_master.고객번호
검색: term_alias.주문 리스트 -> term_master.주문목록
출력: 고객번호를 입력하면 주문목록을 조회한다.
```

### 13.2 바이브코딩 산출물 생성

```text
입력: 고객별 주문 내역 조회 API 만들어줘.
매핑: 고객번호, 주문번호, 주문일자, 주문금액, 주문상태코드
생성: OpenAPI Schema, DTO, SQL
검증: API 필드명과 DB 컬럼명이 term_expression 표준과 일치하는지 확인
```

### 13.3 영향도 분석

```text
질문: CUST_NO를 customerNumber로 노출하는 API는?
Graphify:
Column(CUST_NO) -> represents -> Term(고객번호)
APIField(customerNumber) -> represents -> Term(고객번호)
API(GET /orders) -> uses -> APIField(customerNumber)
```

## 14. 구현 기준 요약

| 항목 | 기준 |
|---|---|
| DB | PostgreSQL |
| API | OpenAPI-first |
| Backend | Kotlin, Spring Boot |
| Frontend | Next.js, React, Tailwind CSS 4 |
| 검색 | 정확 검색, 유사어 검색, 의미 검색 |
| AI | 데이터 사전 우선 조회, 없는 용어는 후보 분리 |
| Graphify | 관계/영향도 분석 전용 |
| RAG | 자연어 검색, 설명, 문서 검토 전용 |
