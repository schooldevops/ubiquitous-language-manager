# Task 05 Result: 신규 용어 후보 워크플로우 구현

## 1. 수행 범위

- Candidate API OpenAPI Spec 작성
- 후보 등록, 목록, 상세, 검토, 승격 API 계약 정의
- Candidate workflow schema 정의
- OpenAPI Generator로 server/client 타입 생성
- 신규 후보 등록 API 구현
- 후보 목록/상세 조회 API 구현
- 후보 검토 API 구현
- 후보 표준 용어 승격 API 구현
- 후보 등록 시 기존 유사 용어 검색 구현
- 후보 상태 변경 이력 기록 구현
- 후보 UI 화면 구현

## 2. 변경 파일

| 파일 | 변경 내용 |
|---|---|
| `openapi/openapi.yaml` | Candidate API path와 schema 참조 추가 |
| `openapi/paths/candidates.yaml` | `/candidates` workflow API 정의 |
| `openapi/schemas/candidate.yaml` | 후보 workflow 요청/응답 schema 정의 |
| `docs/api/candidate-api-spec.md` | Candidate API Spec 설명 문서 추가 |
| `generated/backend` | `CandidateApi`, 후보 workflow model 생성 |
| `generated/frontend` | `CandidateApi` axios client와 후보 workflow model 생성 |
| `apps/backend/src/main/kotlin/com/aulms/candidate/CandidateApiController.kt` | Candidate API 구현 |
| `apps/backend/src/main/kotlin/com/aulms/candidate/CandidateService.kt` | 후보 workflow 비즈니스 로직 구현 |
| `apps/backend/src/main/kotlin/com/aulms/candidate/CandidateRepository.kt` | 후보와 상태 변경 이력 저장소 구현 |
| `apps/backend/src/main/kotlin/com/aulms/candidate/CandidateNotFoundException.kt` | 후보 조회 실패 예외 추가 |
| `apps/backend/src/main/kotlin/com/aulms/term/ApiExceptionHandler.kt` | 후보 not found 예외 처리 추가 |
| `apps/backend/src/test/kotlin/com/aulms/candidate/CandidateApiContractTest.kt` | 후보 등록, 승인, 승격 contract test 추가 |
| `apps/backend/src/test/kotlin/com/aulms/term/TermApiContractTest.kt` | 표준 용어 생성 ID 의존 제거 |
| `apps/frontend/src/lib/term-api.ts` | Candidate API wrapper와 fallback sample 추가 |
| `apps/frontend/src/app/page.tsx` | 후보 등록, 승인, 승격 UI 추가 |
| `docs/api/candidate-api-implementation.md` | Candidate API 구현 설명 문서 추가 |

## 3. 검증

- YAML 문법 검증 통과
- `openapi-generator-cli validate -i openapi/openapi.yaml` 통과
- `./scripts/openapi-generate-backend.sh` 실행 성공
- `./scripts/openapi-generate-frontend.sh` 실행 성공
- `gradle test` 통과
- `npm run typecheck` 통과
- `npm run build` 통과
- `http://localhost:3002` 로컬 렌더링 확인 후 dev server 종료

## 4. 완료 기준 대응

| 완료 기준 | 결과 |
|---|---|
| 미등록 용어가 검토 결과에서 신규 후보로 생성될 수 있다 | `POST /candidates`와 후보 UI로 등록 가능 |
| 후보를 승인하면 `TERM_MASTER`에 반영할 수 있다 | `promoteCandidate`에서 `TermRepository.create`로 `Term` 생성 |
| 후보를 승인하면 `TERM_EXPRESSION`에 반영할 수 있다 | 승격 시 Korean, English, DB_COLUMN, API_FIELD, CODE_VARIABLE, UI_LABEL, TEST_WORD 표현 생성 |
