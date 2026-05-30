# Task 02 결과: OpenAPI 및 DDL 검증기 구현

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, OpenAPI-first 원칙에 따라 개발 산출물 검증 API를 추가하고 OpenAPI/DDL 검증기를 구현했다.

## OpenAPI 변경

- `openapi/openapi.yaml`
  - `/artifact-validations`
  - `ArtifactSourceType`
  - `ArtifactValidationRequest`
  - `ArtifactValidationResult`
  - `ArtifactValidationSummary`
- `openapi/paths/artifact-validations.yaml`
- `openapi/schemas/artifact-validation.yaml`

## Generator 산출물

- 백엔드
  - `generated/backend/src/main/kotlin/com/aulms/model/Artifact*.kt`
  - `generated/backend/src/main/kotlin/com/aulms/api/ReviewApi.kt`
- 프런트
  - `generated/frontend/model/artifact*.ts`
  - `generated/frontend/api/review-api.ts`

## 백엔드 구현

- `ArtifactValidationService`
  - OpenAPI YAML `properties` field 추출
  - DDL `CREATE TABLE` 컬럼명/타입/자릿수/소수점 추출
  - `RuleEngine` 표현 검증 연동
  - 물리 스펙 검증 연동
  - JSON report용 summary/exitCode 생성
- `ReviewApiController`
  - `validateArtifact` operation 구현

## 테스트 fixture

- `apps/backend/src/test/resources/fixtures/artifacts/customer-openapi.yaml`
- `apps/backend/src/test/resources/fixtures/artifacts/customer-ddl.sql`

## 완료 기준 확인

- OpenAPI에서 `customerId` 필드를 발견하면 `customerNumber`를 권장한다.
- DDL에서 `CUST_ID` 컬럼을 발견하면 `CUST_NO`를 권장한다.
- DDL 물리 타입, 자릿수, 소수점 비교를 수행한다.
- Deprecated/Forbidden alias는 Rule Engine 정책에 따라 `ERROR`로 반환한다.
- JSON 검증 리포트 형태로 `summary`, `issues`, `exitCode`를 반환한다.

## 검증 명령

```bash
ruby -e "require 'yaml'; %w[openapi/openapi.yaml openapi/paths/artifact-validations.yaml openapi/schemas/artifact-validation.yaml].each { |f| YAML.load_file(f); puts \"OK #{f}\" }"
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
