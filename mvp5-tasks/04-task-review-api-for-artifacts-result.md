# Task 04 결과: 개발 산출물 Review API 구현

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, OpenAPI-first 원칙에 따라 외부 도구, PR 봇, CI가 호출할 수 있는 Review API를 추가했다.

## OpenAPI 변경

- `openapi/openapi.yaml`
  - `/reviews/openapi`
  - `/reviews/ddl`
  - `/reviews/code`
  - `/reviews/pr`
  - `PullRequestArtifactReviewRequest`
  - `PullRequestArtifactFile`
  - `PullRequestArtifactReviewResult`
- `openapi/paths/reviews.yaml`
- `openapi/schemas/artifact-validation.yaml`

## Generator 산출물

- 백엔드
  - `generated/backend/src/main/kotlin/com/aulms/api/ReviewApi.kt`
  - `generated/backend/src/main/kotlin/com/aulms/model/PullRequestArtifact*.kt`
- 프런트
  - `generated/frontend/api/review-api.ts`
  - `generated/frontend/model/pull-request-artifact*.ts`

## 백엔드 구현

- `ReviewApiController`
  - `reviewOpenApiArtifact`
  - `reviewDdlArtifact`
  - `reviewCodeArtifact`
  - `reviewPullRequestArtifacts`
- `ArtifactValidationService`
  - OpenAPI 전용 wrapper
  - DDL 전용 wrapper
  - 코드 산출물 wrapper
  - PR 다중 파일 검증 및 전체 집계

## 완료 기준 확인

- CI 또는 PR 봇이 HTTP API로 OpenAPI 검증을 요청할 수 있다.
- CI 또는 PR 봇이 HTTP API로 DDL 검증을 요청할 수 있다.
- CI 또는 PR 봇이 HTTP API로 코드 검증을 요청할 수 있다.
- CI 또는 PR 봇이 HTTP API로 PR 변경 파일 묶음 검증을 요청할 수 있다.
- 응답에 파일 위치, 위반 사유, 권장 표현이 포함된다.

## 검증 명령

```bash
ruby -e "require 'yaml'; %w[openapi/openapi.yaml openapi/paths/reviews.yaml openapi/schemas/artifact-validation.yaml].each { |f| YAML.load_file(f); puts \"OK #{f}\" }"
openapi-generator-cli validate -i openapi/openapi.yaml
./scripts/openapi-generate-backend.sh
./scripts/openapi-generate-frontend.sh
gradle test
npm run typecheck
```

## 검증 결과

- OpenAPI YAML 파싱 성공
- OpenAPI validation 성공
  - 기존 경고: `UserRole`, `RelationshipType` 미사용 모델
- 백엔드 OpenAPI generator 실행 성공
- 프런트 OpenAPI generator 실행 성공
- `apps/backend` 기준 `gradle test` 성공
- `apps/frontend` 기준 `npm run typecheck` 성공
