# 데이터 사전 기반 유비쿼터스 랭기지 시스템 개발 플랜

## 1. 개발 목표

`requirements.md`의 요구사항을 기준으로 기존 데이터 사전을 유비쿼터스 랭기지 관리 시스템으로 확장한다. 개발 목표는 다음과 같다.

- 표준 용어, 표현, 별칭, 관계, 승인 상태를 일관되게 관리한다.
- 기획서, API, DB, 코드, 테스트 산출물이 동일한 표준 용어 체계를 사용하도록 한다.
- AI가 사내 데이터 사전을 조회하여 표준 용어를 추천하고 비표준 표현을 경고하도록 한다.
- RAG는 자연어 검색과 문서 검토에 사용하고, Graphify는 관계 검색과 영향도 분석에 사용한다.
- PR/CI 단계에서 비표준 용어 사용을 자동 검출한다.

## 2. 개발 범위

### 2.1 MVP 범위

MVP는 표준 용어 운영과 기본 검증 흐름을 빠르게 사용할 수 있는 수준으로 정의한다.

- 용어 마스터 CRUD
- 용어 표현 매핑 관리
- 유사어, 별칭, 금지어 관리
- 용어 상태 관리
- 정확 검색 및 유사어 검색
- 기획서 텍스트 용어 검토
- 신규 용어 후보 등록
- 기본 API 제공
- 관리자용 웹 화면

### 2.2 2차 범위

- 의미 기반 검색
- RAG 인덱스 구축
- 용어별 Markdown 문서 생성
- AI 프롬프트 템플릿 관리
- OpenAPI, DDL, DTO 검증
- 변경 이력 및 버전 관리 강화

### 2.3 3차 범위

- Graphify 기반 관계 그래프 구축
- 영향도 분석
- PR 리뷰 봇
- CI/CD 품질 게이트
- IDE 또는 MCP 연동
- 도메인별 용어 지도

## 3. 권장 시스템 구성

```text
[Web Admin]
  용어 검색 / 상세 / 등록 / 승인 / 영향도 분석
        |
        v
[Backend API]
  Term API / Search API / Review API / Governance API
        |
        v
[Core Services]
  Term Service
  Expression Mapping Service
  Alias Service
  Rule Validation Service
  Candidate Term Service
  Document Review Service
        |
        +------------------+
        |                  |
        v                  v
[Relational DB]       [Search / RAG Index]
  표준 용어              자연어 검색
  표현 매핑              기획서 검토
  별칭/금지어             AI 질의응답
  관계/이력
        |
        v
[Graphify]
  관계 검색 / 영향도 분석 / 시스템 사용 현황
```

## 4. 데이터베이스 개발 플랜

### 4.1 1차 스키마

다음 테이블을 우선 구현한다.

- `TERM_MASTER`
- `TERM_EXPRESSION`
- `TERM_ALIAS`
- `TERM_RELATIONSHIP`
- `TERM_CHANGE_HISTORY`
- `TERM_REVIEW_REQUEST`
- `TERM_CANDIDATE`

### 4.2 주요 설계 기준

- `TERM_ID`는 내부 식별자로 사용한다.
- `TERM_NO`는 업무적으로 노출 가능한 번호로 사용한다.
- `STATUS`는 `Draft`, `Reviewing`, `Approved`, `Deprecated`, `Rejected` 중 하나만 허용한다.
- `TERM_EXPRESSION`은 DB 컬럼명, API 필드명, 코드 변수명, UI 라벨, 테스트 용어를 모두 표현 유형으로 관리한다.
- `TERM_ALIAS`는 유사어, 별칭, 금지어를 구분한다.
- `TERM_RELATIONSHIP`은 Graphify 연동 전에도 관계 데이터를 저장할 수 있도록 선행 구축한다.
- 모든 변경은 `TERM_CHANGE_HISTORY`에 기록한다.

### 4.3 마이그레이션 작업

1. 기존 데이터 사전 컬럼을 분석한다.
2. 중복 용어와 중복 약어를 식별한다.
3. 기존 항목을 `TERM_MASTER`로 이관한다.
4. DB 컬럼명, API 필드명, 화면 표시명 등 표현 값을 `TERM_EXPRESSION`으로 분리한다.
5. 폐기 용어와 혼동 용어를 `TERM_ALIAS`에 등록한다.
6. 도메인, 소유자, 상태 기본값을 부여한다.

## 5. Backend API 개발 플랜

### 5.1 Term API

- `POST /terms`: 용어 등록
- `GET /terms`: 용어 목록 조회
- `GET /terms/{termId}`: 용어 상세 조회
- `PUT /terms/{termId}`: 용어 수정
- `POST /terms/{termId}/approve`: 용어 승인
- `POST /terms/{termId}/deprecate`: 용어 폐기
- `GET /terms/{termId}/history`: 변경 이력 조회

### 5.2 Expression API

- `POST /terms/{termId}/expressions`: 표현 추가
- `PUT /terms/{termId}/expressions/{expressionId}`: 표현 수정
- `DELETE /terms/{termId}/expressions/{expressionId}`: 표현 삭제
- `GET /terms/{termId}/expressions`: 표현 목록 조회

### 5.3 Alias API

- `POST /terms/{termId}/aliases`: 별칭, 유사어, 금지어 등록
- `GET /terms/{termId}/aliases`: 별칭 목록 조회
- `PUT /terms/{termId}/aliases/{aliasId}`: 별칭 수정
- `DELETE /terms/{termId}/aliases/{aliasId}`: 별칭 삭제

### 5.4 Search API

- `GET /search/exact?q=`: 정확 검색
- `GET /search/alias?q=`: 유사어 검색
- `POST /search/semantic`: 의미 기반 검색
- `GET /search/domain/{domainName}`: 도메인별 용어 조회
- `GET /search/deprecated?q=`: 폐기 용어 및 대체 용어 조회

### 5.5 Review API

- `POST /reviews/document`: 기획서 텍스트 용어 검토
- `POST /reviews/openapi`: OpenAPI 필드명 검토
- `POST /reviews/ddl`: DB DDL 컬럼명 검토
- `POST /reviews/code`: 코드 변수명 검토
- `POST /reviews/pr`: PR 변경 파일 검토

### 5.6 Candidate API

- `POST /candidates`: 신규 용어 후보 등록
- `GET /candidates`: 후보 목록 조회
- `GET /candidates/{candidateId}`: 후보 상세 조회
- `POST /candidates/{candidateId}/review`: 후보 검토
- `POST /candidates/{candidateId}/promote`: 후보를 표준 용어로 승격

## 6. Frontend 개발 플랜

### 6.1 화면 목록

1. 용어 검색 화면
2. 용어 상세 화면
3. 용어 등록/수정 화면
4. 신규 용어 신청 화면
5. 승인 대기 목록 화면
6. 기획서 용어 검토 화면
7. 표준 위반 검토 화면
8. 영향도 분석 화면
9. 변경 이력 화면
10. 관리 설정 화면

### 6.2 MVP 화면 우선순위

1. 용어 검색 화면
2. 용어 상세 화면
3. 용어 등록/수정 화면
4. 신규 용어 신청 화면
5. 기획서 용어 검토 화면

### 6.3 화면별 핵심 기능

| 화면 | 핵심 기능 |
|---|---|
| 용어 검색 | 한글명, 영문명, 약어, API 필드명, 별칭 검색 |
| 용어 상세 | 정의, 표현 매핑, 별칭, 관계, 이력, 상태 표시 |
| 용어 등록/수정 | 표준 용어와 산출물 표현 입력 |
| 신규 용어 신청 | 후보 등록, 유사 용어 확인, 승인 요청 |
| 기획서 검토 | 문장 입력 또는 파일 업로드, 비표준 용어 탐지 |
| 표준 위반 검토 | DDL, OpenAPI, 코드 텍스트 검증 |
| 영향도 분석 | 용어와 연결된 시스템, 테이블, API, 문서 조회 |

## 7. 검색 개발 플랜

### 7.1 정확 검색

다음 필드를 대상으로 exact match 및 normalized match를 제공한다.

- 한글 용어명
- 영문 용어명
- 영문 약어
- DB 컬럼명
- API 필드명
- 코드 변수명
- 화면 표시명
- 테스트 용어

### 7.2 유사어 검색

다음 데이터를 기반으로 표준 용어를 추천한다.

- `TERM_ALIAS`
- 폐기 용어
- 금지어
- 자주 혼동되는 표현
- 대소문자, 공백, 하이픈, 언더스코어 정규화 값

### 7.3 의미 기반 검색

2차 개발에서 RAG 또는 임베딩 검색을 도입한다.

작업 순서:

1. 용어별 검색 문서를 생성한다.
2. 업무 정의, 사용 맥락, 예시 문장, 별칭을 하나의 문서로 묶는다.
3. 임베딩을 생성한다.
4. 자연어 질의와 유사한 용어 후보를 반환한다.
5. Rule Engine으로 상태, 금지어, 폐기어를 후처리한다.

## 8. AI 기능 개발 플랜

### 8.1 기획서 용어 검토

처리 흐름:

```text
기획서 입력
  -> 문장 분리
  -> 업무 용어 후보 추출
  -> 정확 검색
  -> 유사어 검색
  -> 의미 검색
  -> 표준 용어 매핑
  -> 비표준 표현 경고
  -> 신규 용어 후보 생성
  -> 용어 매핑표 출력
```

### 8.2 바이브코딩 지원

AI 개발 요청에 대해 다음 결과를 생성한다.

- 업무 개념 추출
- 데이터 사전 매핑
- DB 컬럼명 추천
- API 필드명 추천
- DTO 변수명 추천
- 신규 용어 후보
- 표준 위반 경고

### 8.3 신규 용어 후보 추천

AI는 신규 용어를 바로 확정하지 않고 다음 정보만 제안한다.

- 한글 용어명 후보
- 영문 용어명 후보
- 영문 약어 후보
- API 필드명 후보
- 코드 변수명 후보
- 물리 타입 후보
- 자릿수 후보
- 신규 사유
- 승인 필요 여부

## 9. Rule Engine 개발 플랜

### 9.1 검증 규칙

Rule Engine은 다음 규칙을 제공해야 한다.

- Approved 용어만 개발 산출물에 사용 가능
- Draft 용어는 기획서에서만 후보로 사용 가능
- Deprecated 용어는 사용 금지
- 금지어는 표준 표현으로 대체 권고
- API 필드명은 `TERM_EXPRESSION`의 `API_FIELD` 값과 일치해야 함
- DB 컬럼명은 `TERM_EXPRESSION`의 `DB_COLUMN` 값과 일치해야 함
- 코드 변수명은 `TERM_EXPRESSION`의 `CODE_VARIABLE` 값과 일치해야 함
- 물리 타입, 자릿수, 소수점은 `TERM_MASTER` 기준과 일치해야 함

### 9.2 검증 결과 형식

검증 결과는 다음 구조를 따른다.

| 항목 | 설명 |
|---|---|
| severity | ERROR, WARNING, INFO |
| source | 문서, DDL, OpenAPI, 코드, PR |
| location | 파일명, 라인, 필드명 등 위치 정보 |
| inputExpression | 입력 표현 |
| standardTerm | 표준 용어 |
| recommendedExpression | 권장 표현 |
| reason | 위반 또는 권고 사유 |

## 10. RAG 개발 플랜

### 10.1 용어 문서 생성

각 용어는 다음 형식의 Markdown 문서로 생성한다.

```markdown
# 고객번호

- 영문명: Customer Number
- DB 컬럼명: CUST_NO
- API 필드명: customerNumber
- 코드 변수명: customerNumber
- 상태: Approved
- 업무 정의: 회사에서 고객을 업무적으로 식별하기 위해 사용하는 번호
- 사용 맥락: 주문, 계약, 청구, 상담 등에서 고객 식별 기준으로 사용
- 유사어: 고객ID, 고객식별자, customerId
- 금지어: CUST_ID
- 관련 용어: 고객명, 고객상태코드, 주문번호
```

### 10.2 인덱싱

- Approved 용어를 우선 인덱싱한다.
- Draft 및 Reviewing 용어는 후보 검색용 인덱스로 분리한다.
- Deprecated 용어는 대체 용어 안내용 인덱스로 분리한다.
- 용어 변경 승인 후 인덱스를 증분 갱신한다.

### 10.3 검색 후처리

- 폐기 용어가 검색되면 대체 용어를 상단에 노출한다.
- Draft 용어는 개발 사용 불가 경고를 붙인다.
- 유사도 점수만으로 확정하지 않고 Rule Engine 결과와 함께 반환한다.

## 11. Graphify 개발 플랜

### 11.1 그래프 노드

Graphify에는 다음 노드를 생성한다.

- Term
- Domain
- System
- Table
- Column
- API
- API Field
- DTO
- Entity
- Document
- Test Case

### 11.2 그래프 관계

다음 관계를 생성한다.

- Term sameAs Term
- Term synonymOf Term
- Term relatedTo Term
- Term usedWith Term
- Term deprecatedBy Term
- Term belongsTo Domain
- Column represents Term
- APIField represents Term
- DTOField represents Term
- Document mentions Term
- TestCase uses Term
- System owns Table
- API uses APIField

### 11.3 영향도 분석

지원 질의:

- 특정 용어를 사용하는 시스템 조회
- 특정 DB 컬럼을 사용하는 API 조회
- 특정 API 필드와 연결된 DTO 조회
- 폐기 용어를 사용하는 테이블 조회
- 특정 용어 변경 시 영향받는 문서, 테스트, 코드 조회

## 12. PR 및 CI/CD 개발 플랜

### 12.1 PR 리뷰 봇

PR 리뷰 봇은 변경 파일에서 다음 항목을 분석한다.

- DDL 파일의 컬럼명
- OpenAPI YAML의 필드명
- DTO, Entity, Request, Response 필드명
- SQL Mapper의 컬럼명
- 테스트 코드의 업무 용어
- 화면 라벨 텍스트

### 12.2 CI 품질 게이트

CI에서는 다음 정책을 적용한다.

- ERROR 위반이 있으면 빌드를 실패시킨다.
- WARNING은 빌드를 통과시키되 리포트를 남긴다.
- Deprecated 용어 신규 사용은 ERROR로 처리한다.
- Draft 용어 개발 산출물 사용은 ERROR로 처리한다.
- 후보 용어는 별도 리포트로 생성한다.

### 12.3 리포트 형식

```text
[데이터 사전 표준 위반]

파일: CustomerOrderResponse.kt
라인: 12
현재 코드: val customerId: String
권장 코드: val customerNumber: String
사유: '고객번호'의 표준 API/코드 표현은 customerNumber입니다.
심각도: ERROR
```

## 13. 개발 단계별 일정

### 13.1 Phase 0: 프로젝트 기반 정리

목표: 개발 착수 전 기준을 확정한다.

주요 작업:

- 기술 스택 확정
- 기존 데이터 사전 샘플 확보
- 도메인 및 사용자 역할 정의
- API 응답 표준 정의
- DB 명명 규칙 확정
- 배포 환경 확정

완료 기준:

- 개발 환경이 실행 가능하다.
- 샘플 데이터로 용어 검색 시나리오를 검증할 수 있다.

### 13.2 Phase 1: 데이터 사전 코어

목표: 표준 용어를 등록, 조회, 수정, 승인할 수 있게 한다.

주요 작업:

- DB 스키마 구현
- Term API 구현
- Expression API 구현
- Alias API 구현
- 변경 이력 기록
- 기본 관리자 화면 구현
- 샘플 데이터 적재

완료 기준:

- 고객번호 예시를 등록하고 API, DB, 코드 표현을 연결할 수 있다.
- 고객ID 검색 시 고객번호를 추천할 수 있다.

### 13.3 Phase 2: 검색 및 검증

목표: 표준 용어 검색과 비표준 표현 검증을 제공한다.

주요 작업:

- 정확 검색 구현
- 유사어 검색 구현
- 상태 기반 추천 정책 구현
- Rule Engine 구현
- 기획서 텍스트 검토 구현
- 신규 용어 후보 생성 구현

완료 기준:

- 기획서 문장 `고객 ID를 입력하면 주문 리스트를 조회한다.`를 표준 문장으로 추천할 수 있다.
- Deprecated 또는 금지어 사용 시 대체 용어를 안내할 수 있다.

### 13.4 Phase 3: RAG 및 AI 연동

목표: 자연어 기반 용어 검색과 AI 산출물 생성을 지원한다.

주요 작업:

- 용어별 Markdown 문서 생성
- 임베딩 생성 및 인덱싱
- 의미 기반 검색 API 구현
- 기획서 용어 매핑표 자동 생성
- 바이브코딩용 프롬프트 템플릿 제공
- 신규 용어 후보 추천 고도화

완료 기준:

- `주문이 발생한 날짜` 검색 시 주문일자와 주문일시를 후보로 제시하고 차이를 설명할 수 있다.
- 요구사항 입력 시 업무 개념 추출, 표준 용어 매핑, 신규 후보 리포트를 생성할 수 있다.

### 13.5 Phase 4: Graphify 및 영향도 분석

목표: 관계 그래프와 영향도 분석을 제공한다.

주요 작업:

- 그래프 노드 모델링
- 그래프 관계 모델링
- 데이터 사전에서 Graphify용 데이터 생성
- 관계 검색 API 구현
- 영향도 분석 화면 구현
- 폐기 용어 사용 현황 조회 구현

완료 기준:

- `CUST_NO를 사용하는 시스템`을 조회할 수 있다.
- `고객번호` 변경 시 영향받는 DB, API, DTO, 문서를 조회할 수 있다.

### 13.6 Phase 5: 개발 프로세스 연동

목표: 개발 산출물 검증을 자동화한다.

주요 작업:

- DDL 검증기 구현
- OpenAPI 검증기 구현
- DTO/Entity 검증기 구현
- PR 리뷰 봇 구현
- CI 품질 게이트 구현
- 위반 리포트 저장 및 조회

완료 기준:

- `customerId` 신규 사용을 PR에서 탐지하고 `customerNumber`를 권장할 수 있다.
- ERROR 위반 시 CI가 실패한다.

## 14. 우선순위 백로그

### 14.1 P0

- `TERM_MASTER` 스키마
- `TERM_EXPRESSION` 스키마
- `TERM_ALIAS` 스키마
- 용어 등록/수정/조회 API
- 정확 검색
- 유사어 검색
- 용어 검색 화면
- 용어 상세 화면

### 14.2 P1

- 승인 상태 관리
- 변경 이력
- 신규 용어 후보
- 기획서 용어 검토
- Rule Engine
- 표준 위반 리포트
- 용어 등록/수정 화면

### 14.3 P2

- 의미 기반 검색
- RAG 인덱스
- 프롬프트 템플릿 관리
- OpenAPI 검증
- DDL 검증
- 코드 변수명 검증

### 14.4 P3

- Graphify 관계 구축
- 영향도 분석
- PR 리뷰 봇
- CI 품질 게이트
- IDE 또는 MCP 연동

## 15. 테스트 전략

### 15.1 단위 테스트

- 용어 상태 전이 검증
- 별칭 검색 검증
- 금지어 검증
- 표현 매핑 검증
- Rule Engine 검증

### 15.2 통합 테스트

- 용어 등록 후 검색 API 반영
- 별칭 등록 후 표준 용어 추천
- 용어 폐기 후 대체 용어 안내
- 기획서 검토 후 용어 매핑표 생성
- 신규 용어 후보 생성

### 15.3 E2E 테스트

- 관리자가 용어를 등록하고 승인한다.
- 기획자가 기획서를 검토한다.
- AI가 표준 용어 기반 DTO를 생성한다.
- 개발자가 PR을 생성한다.
- PR 봇이 표준 위반을 코멘트한다.
- CI가 ERROR 위반을 차단한다.

## 16. 운영 및 배포 플랜

### 16.1 배포 단위

- Backend API
- Web Admin
- Search/RAG Worker
- Graphify Sync Worker
- PR Review Bot
- CI Validator CLI

### 16.2 운영 작업

- 용어 승인 권한 관리
- 데이터 사전 정기 정비
- 폐기 용어 사용 현황 점검
- RAG 인덱스 갱신 상태 확인
- Graphify 동기화 상태 확인
- PR 검증 실패 사유 모니터링

### 16.3 모니터링 지표

- 일별 용어 검색 건수
- 비표준 용어 탐지 건수
- 신규 용어 후보 등록 건수
- Approved 전환 건수
- Deprecated 용어 사용 건수
- PR 표준 위반 건수
- RAG 검색 정확도 피드백

## 17. 주요 리스크와 대응

| 리스크 | 영향 | 대응 |
|---|---|---|
| 기존 데이터 사전 품질 낮음 | 검색과 추천 품질 저하 | 1단계에서 중복, 누락, 폐기 용어 정비 |
| 용어 승인 프로세스 지연 | 개발 일정 지연 | Draft와 Approved 사용 정책을 명확히 분리 |
| AI가 표준 외 용어 생성 | 코드 품질 저하 | Rule Engine과 PR 검증으로 차단 |
| RAG 검색 오탐 | 잘못된 용어 추천 | 정확 검색, 별칭 검색, Rule Engine 후처리 병행 |
| Graphify 관계 데이터 부족 | 영향도 분석 부정확 | API, DB, 코드, 문서 스캔 데이터를 단계적으로 축적 |
| 개발자 반발 | 도입률 저하 | PR 코멘트에 사유와 권장 코드까지 제공 |

## 18. 착수 체크리스트

- 기존 데이터 사전 샘플 확보
- 표준 상태값 확정
- 도메인 목록 확정
- 사용자 역할과 권한 확정
- DBMS 및 검색 엔진 확정
- RAG 임베딩 모델 확정
- Graphify 연동 방식 확정
- PR/CI 대상 저장소 확정
- MVP 완료 기준 합의

