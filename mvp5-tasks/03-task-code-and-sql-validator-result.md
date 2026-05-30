# Task 03 결과: 코드 및 SQL 검증기 구현

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, 기존 `POST /artifact-validations` API를 OpenAPI-first로 확장하고 코드/SQL/테스트 검증기를 구현했다.

## OpenAPI 변경

- `openapi/schemas/artifact-validation.yaml`
  - `ArtifactSourceType`에 `KOTLIN`, `JAVA`, `TYPESCRIPT`, `SQL`, `TEST` 추가

## Generator 산출물

- 백엔드
  - `generated/backend/src/main/kotlin/com/aulms/model/ArtifactSourceType.kt`
- 프런트
  - `generated/frontend/model/artifact-source-type.ts`

## 백엔드 구현

- `ArtifactValidationService`
  - Kotlin `val/var` property 추출
  - Java field 추출
  - TypeScript interface/type property 추출
  - `@Column(name = "...")` DB 컬럼 추출
  - `@JsonProperty("...")` API 필드 추출
  - SQL Mapper DB 컬럼과 parameter 추출
  - 테스트 코드 한글 업무 용어와 코드 변수 추출
  - 파일명과 라인 번호 계산

## 테스트 fixture

- `apps/backend/src/test/resources/fixtures/artifacts/customer-dto.kt`
- `apps/backend/src/test/resources/fixtures/artifacts/customer-mapper.sql`
- `apps/backend/src/test/resources/fixtures/artifacts/customer-order.feature`

## 완료 기준 확인

- `val customerId: String`을 탐지하고 `customerNumber`를 권장한다.
- 테스트 문장의 `고객 ID`를 탐지하고 `고객번호`를 권장한다.
- SQL Mapper의 `CUST_ID`를 탐지하고 `CUST_NO`를 권장한다.
- SQL parameter `:customerId`를 탐지하고 `customerNumber`를 권장한다.
- 각 위반에 파일명과 라인 번호를 포함한다.
- `recommendedExpression`이 있는 단일 표현 치환은 자동 수정 가능 대상으로 볼 수 있다.

## 검증 명령

```bash
ruby -e "require 'yaml'; %w[openapi/openapi.yaml openapi/schemas/artifact-validation.yaml].each { |f| YAML.load_file(f); puts \"OK #{f}\" }"
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
