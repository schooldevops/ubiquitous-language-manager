# AULMS

AULMS는 데이터 사전을 유비쿼터스 랭기지로 확장해 기획서, OpenAPI, DB, 코드, 테스트, PR/CI가 같은 표준 용어를 쓰도록 돕는 시스템이다.

핵심 목표:

- 표준 용어, 산출물 표현, 별칭, 금지어, 관계, 승인 상태 관리
- `고객ID`, `customerId`, `CUST_ID` 같은 비표준 표현을 `고객번호`, `customerNumber`, `CUST_NO`로 표준화
- OpenAPI-first 방식으로 서버 stub과 프런트 axios client 생성
- RAG 기반 의미 검색, Graphify 기반 관계/영향도 분석
- PR/CI에서 OpenAPI, DDL, DTO, SQL, 테스트 용어 자동 검증

## 구성

| 영역 | 위치 | 설명 |
|---|---|---|
| 요구사항 | [requirements.md](requirements.md) | 데이터 사전을 유비쿼터스 랭기지로 확장하기 위한 원 요구사항 |
| 개발 계획 | [plan.md](plan.md) | MVP 단계별 개발 계획 |
| OpenAPI Spec | [openapi/openapi.yaml](openapi/openapi.yaml) | 전체 API 진입 spec |
| Backend | [apps/backend](apps/backend) | Kotlin, Spring Boot API 서버 |
| Frontend | [apps/frontend](apps/frontend) | Next.js, React, Tailwind CSS 관리자 화면 |
| Backend generated stub | [generated/backend](generated/backend) | OpenAPI Generator로 생성한 Kotlin Spring server stub |
| Frontend generated client | [generated/frontend](generated/frontend) | OpenAPI Generator로 생성한 TypeScript axios client |
| 개발 문서 | [docs](docs) | 설계, API, RAG, Graphify, 검증기 문서 |
| MVP 태스크 | [mvp0-tasks](mvp0-tasks) ~ [mvp5-tasks](mvp5-tasks) | 단계별 태스크와 결과 |

## 주요 기능

| 기능 | 설명 | 관련 문서 |
|---|---|---|
| 용어 관리 | 표준 용어 등록, 조회, 수정, 승인, 폐기 | [Term API](docs/api/term-api-spec.md) |
| 표현 매핑 | DB 컬럼명, API 필드명, 코드 변수명, UI 라벨, 테스트 용어 연결 | [Core Schema](docs/database/core-schema.md) |
| 별칭/금지어 | 유사어, 금지어, 폐기어를 표준 용어로 매핑 | [Rule Engine](docs/api/rule-engine.md) |
| 검색 | 정확 검색, 유사어 검색, 의미 기반 검색 | [Search API](docs/api/search-api-spec.md) |
| 기획서 검토 | 기획서 문장 내 비표준 용어 검출 | [Document Review](docs/api/document-review-api-spec.md) |
| 신규 후보 | 데이터 사전에 없는 용어 후보 등록, 검토, 승격 | [Candidate API](docs/api/candidate-api-spec.md) |
| RAG | 용어별 Markdown 문서 생성과 의미 검색 | [RAG 문서 생성](docs/rag/rag-document-generation.md) |
| Graphify | 용어 관계 그래프, 관계 검색, 영향도 분석 | [Graph Model](docs/graph/graph-model-design.md) |
| 산출물 검증 | OpenAPI, DDL, DTO, SQL, 테스트 표준 위반 검증 | [Artifact Validation](docs/api/artifact-validation-api-spec.md) |
| PR/CI 연동 | PR 변경 파일 자동 검증, Markdown 리포트, CI gate | [PR/CI Gate](docs/api/pr-review-ci-gate.md) |

## 서버 실행

서버는 `apps/backend`에서 실행한다. 루트에는 Gradle 빌드 파일이 없으므로 반드시 백엔드 디렉토리로 이동한다.

```bash
cd apps/backend
gradle bootRun
```

기본 URL:

```text
http://localhost:8080
```

테스트:

```bash
cd apps/backend
gradle test
```

## 클라이언트 실행

프런트는 `apps/frontend`에서 실행한다.

```bash
cd apps/frontend
npm install
npm run dev
```

기본 URL:

```text
http://localhost:3000
```

환경 변수 예시:

```bash
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
NEXT_PUBLIC_API_TOKEN=local-dev-token
```

타입 체크:

```bash
cd apps/frontend
npm run typecheck
```

## OpenAPI 생성

API 개발은 OpenAPI spec을 먼저 작성한 뒤 generated stub/client를 생성하는 방식으로 진행한다.

OpenAPI 검증:

```bash
openapi-generator-cli validate -i openapi/openapi.yaml
```

백엔드 server stub 생성:

```bash
./scripts/openapi-generate-backend.sh
```

프런트 axios client 생성:

```bash
./scripts/openapi-generate-frontend.sh
```

생성된 파일:

| 대상 | 위치 |
|---|---|
| Backend API interface | [generated/backend/src/main/kotlin/com/aulms/api](generated/backend/src/main/kotlin/com/aulms/api) |
| Backend model | [generated/backend/src/main/kotlin/com/aulms/model](generated/backend/src/main/kotlin/com/aulms/model) |
| Frontend API client | [generated/frontend/api](generated/frontend/api) |
| Frontend model | [generated/frontend/model](generated/frontend/model) |

## Review API 사용

외부 도구, PR 봇, CI는 Review API를 호출한다.

| API | 용도 |
|---|---|
| `POST /reviews/openapi` | OpenAPI YAML API field 검증 |
| `POST /reviews/ddl` | DB DDL 컬럼명/물리 스펙 검증 |
| `POST /reviews/code` | DTO, Entity, SQL, 테스트 용어 검증 |
| `POST /reviews/pr` | PR 변경 파일 묶음 검증 |

예시 요청:

```json
{
  "sourceType": "KOTLIN",
  "filePath": "CustomerOrderResponse.kt",
  "content": "data class CustomerOrderResponse(val customerId: String)",
  "includeSuggestions": true
}
```

예시 결과:

```json
{
  "severity": "WARNING",
  "source": "KOTLIN",
  "location": "CustomerOrderResponse.kt:1",
  "inputExpression": "customerId",
  "standardTerm": "고객번호",
  "recommendedExpression": "customerNumber",
  "reason": "권장 표현은 customerNumber입니다."
}
```

## PR/CI 검증 실행

변경 파일 목록 생성:

```bash
git diff --name-only origin/main...HEAD > changed-files.txt
```

PR 검증 CLI 실행:

```bash
ruby scripts/aulms-pr-review.rb \
  --changed-files changed-files.txt \
  --output-json build/reports/aulms-pr-review.json \
  --output-md build/reports/aulms-pr-review.md \
  --fail-on error
```

샘플 E2E:

```bash
ruby scripts/test-aulms-pr-review.rb
```

GitHub Actions workflow:

- [.github/workflows/aulms-artifact-review.yml](.github/workflows/aulms-artifact-review.yml)

필요 secret:

| Secret | 설명 |
|---|---|
| `AULMS_API_URL` | AULMS API base URL |
| `AULMS_API_TOKEN` | AULMS API bearer token |

## 생성과정

| 단계 | 목표 | 주요 산출물 | 결과 문서 |
|---|---|---|---|
| Requirements | 유비쿼터스 랭기지 요구사항 정리 | [requirements.md](requirements.md) | [requirements.md](requirements.md) |
| Plan | MVP 개발 계획 수립 | [plan.md](plan.md) | [plan.md](plan.md) |
| MVP0-01 | 프로젝트 기반 정리 | 기술 스택, 구조 | [결과](mvp0-tasks/01-task-project-foundation-result.md) |
| MVP0-02 | 도메인/역할 정의 | 도메인, 권한 모델 | [결과](mvp0-tasks/02-task-domain-and-role-definition-result.md) |
| MVP0-03 | 샘플 데이터 준비 | 용어/표현/별칭 샘플 | [결과](mvp0-tasks/03-task-sample-data-preparation-result.md) |
| MVP0-04 | OpenAPI 기준 수립 | OpenAPI baseline | [결과](mvp0-tasks/04-task-openapi-baseline-result.md) |
| MVP1-01 | 데이터베이스 코어 설계 | core schema, ERD | [결과](mvp1-tasks/01-task-database-schema-core-result.md) |
| MVP1-02 | Term API spec | Term/Expression/Alias/Governance API | [결과](mvp1-tasks/02-task-term-openapi-spec-result.md) |
| MVP1-03 | Term API 구현 | 백엔드 용어 관리 API | [결과](mvp1-tasks/03-task-term-api-implementation-result.md) |
| MVP1-04 | 관리자 화면 | 용어 검색/상세/등록 UI | [결과](mvp1-tasks/04-task-admin-term-screens-result.md) |
| MVP2-01 | Search API spec | 정확/유사어/도메인 검색 | [결과](mvp2-tasks/01-task-search-openapi-spec-result.md) |
| MVP2-02 | 검색/별칭 구현 | 고객ID -> 고객번호 추천 | [결과](mvp2-tasks/02-task-search-and-alias-implementation-result.md) |
| MVP2-03 | Rule Engine | 표준 위반 severity 정책 | [결과](mvp2-tasks/03-task-rule-engine-result.md) |
| MVP2-04 | 기획서 검토 | 문장 표준화, mapping table | [결과](mvp2-tasks/04-task-document-review-openapi-and-implementation-result.md) |
| MVP2-05 | 신규 후보 흐름 | 후보 등록/검토/승격 | [결과](mvp2-tasks/05-task-candidate-term-workflow-result.md) |
| MVP3-01 | RAG 문서 생성 | 용어별 Markdown 문서 | [결과](mvp3-tasks/01-task-rag-document-generation-result.md) |
| MVP3-02 | 의미 검색 | 자연어 기반 용어 검색 | [결과](mvp3-tasks/02-task-semantic-search-openapi-and-implementation-result.md) |
| MVP3-03 | 프롬프트 템플릿 | 기획/바이브코딩 prompt 관리 | [결과](mvp3-tasks/03-task-ai-prompt-template-management-result.md) |
| MVP3-04 | AI 산출물 생성 | 요구사항 -> API/DTO/테스트 초안 | [결과](mvp3-tasks/04-task-ai-assisted-output-generation-result.md) |
| MVP4-01 | Graph 모델 설계 | Term/Domain/System/API/DTO 관계 | [결과](mvp4-tasks/01-task-graph-model-design-result.md) |
| MVP4-02 | Graph sync worker | 데이터 사전 -> graph snapshot | [결과](mvp4-tasks/02-task-graph-sync-worker-result.md) |
| MVP4-03 | 관계 검색 API | `CUST_NO` 사용 시스템, 관련 용어 조회 | [결과](mvp4-tasks/03-task-relationship-search-openapi-and-implementation-result.md) |
| MVP4-04 | 영향도 분석 | 고객번호 변경 시 DB/API/DTO/문서/테스트 영향 조회 | [결과](mvp4-tasks/04-task-impact-analysis-result.md) |
| MVP5-01 | 산출물 검증기 설계 | Parser, CLI, PR format 설계 | [결과](mvp5-tasks/01-task-artifact-validator-design-result.md) |
| MVP5-02 | OpenAPI/DDL 검증 | `customerId`, `CUST_ID` 검출 | [결과](mvp5-tasks/02-task-openapi-and-ddl-validator-result.md) |
| MVP5-03 | 코드/SQL/테스트 검증 | DTO field, SQL mapper, 테스트 용어 검증 | [결과](mvp5-tasks/03-task-code-and-sql-validator-result.md) |
| MVP5-04 | Review API | `/reviews/openapi`, `/reviews/ddl`, `/reviews/code`, `/reviews/pr` | [결과](mvp5-tasks/04-task-review-api-for-artifacts-result.md) |
| MVP5-05 | PR/CI gate | PR bot, CLI, GitHub Actions | [결과](mvp5-tasks/05-task-pr-review-bot-and-ci-gate-result.md) |
| MVP5-06 | 개발자 가이드 | CLI/API/CI 대응 가이드 | [결과](mvp5-tasks/06-task-developer-integration-guide-result.md) |

## 주요 문서 링크

| 문서 | 설명 |
|---|---|
| [개발자 연동 가이드](docs/api/developer-integration-guide.md) | 서버 API, client, CLI, PR/CI 사용법 |
| [CLI 사용 예시](docs/api/cli-usage-examples.md) | 로컬 검증 명령 |
| [PR/CI 대응 가이드](docs/api/pr-ci-troubleshooting.md) | CI 실패 대응 |
| [산출물 검증 API](docs/api/artifact-validation-api-spec.md) | Review API schema |
| [PR/CI Gate](docs/api/pr-review-ci-gate.md) | PR bot 및 GitHub Actions 구조 |
| [ERD 종합 문서](erd.md) | 유비쿼터스 랭기지 저장 테이블, ERD, 적재/활용 흐름 |
| [OpenAPI 개발 규칙](docs/foundation/03-api-first-development-rules.md) | OpenAPI-first 개발 절차 |

## 현재 상태

작성된 MVP 태스크 기준으로 `mvp0`부터 `mvp5`까지 모든 result 문서가 존재한다. 루트 기준 전체 수행 산출물은 [mvp0-tasks](mvp0-tasks), [mvp1-tasks](mvp1-tasks), [mvp2-tasks](mvp2-tasks), [mvp3-tasks](mvp3-tasks), [mvp4-tasks](mvp4-tasks), [mvp5-tasks](mvp5-tasks)에서 확인할 수 있다.
