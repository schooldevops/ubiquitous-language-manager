# Artifact Validation 구현

## 구현 범위

- `POST /artifact-validations`
- `POST /reviews/openapi`
- `POST /reviews/ddl`
- `POST /reviews/code`
- `POST /reviews/pr`
- OpenAPI YAML property parser
- DDL column/type parser
- Kotlin/Java/TypeScript field parser
- SQL Mapper parser
- 테스트 한글 업무 용어 parser
- Rule Engine 연동
- JSON 검증 리포트
- fixture 기반 contract test

## 구현 파일

| 파일 | 설명 |
|---|---|
| `openapi/paths/artifact-validations.yaml` | API path spec |
| `openapi/schemas/artifact-validation.yaml` | 요청/응답 schema |
| `apps/backend/src/main/kotlin/com/aulms/review/ArtifactValidationService.kt` | OpenAPI/DDL 검증 서비스 |
| `apps/backend/src/test/resources/fixtures/artifacts/customer-dto.kt` | Kotlin DTO fixture |
| `apps/backend/src/test/resources/fixtures/artifacts/customer-mapper.sql` | SQL mapper fixture |
| `apps/backend/src/test/resources/fixtures/artifacts/customer-order.feature` | 테스트 문장 fixture |
| `apps/backend/src/main/kotlin/com/aulms/review/ReviewApiController.kt` | generated `ReviewApi.validateArtifact` 구현 |
| `apps/backend/src/test/kotlin/com/aulms/artifact/ArtifactValidationApiContractTest.kt` | contract test |

## Review API Wrappers

외부 도구, PR 봇, CI는 목적별 endpoint를 호출한다.

| Endpoint | 동작 |
|---|---|
| `/reviews/openapi` | 요청 sourceType과 무관하게 `OPENAPI`로 검증 |
| `/reviews/ddl` | 요청 sourceType과 무관하게 `DDL`로 검증 |
| `/reviews/code` | `KOTLIN`, `JAVA`, `TYPESCRIPT`, `SQL`, `TEST` 또는 파일 확장자 기반 판별 |
| `/reviews/pr` | 여러 파일을 검증하고 파일별 결과와 전체 집계를 반환 |

PR 검증은 각 파일을 `ArtifactValidationRequest`로 변환한 뒤 기존 검증기를 재사용한다.

## OpenAPI Validator

OpenAPI YAML은 SnakeYAML로 parse한다. 모든 `properties` map을 순회해 schema property name을 추출한다.

추출한 property name은 `ExpressionType.API_FIELD`로 Rule Engine에 전달한다.

예:

| 입력 | 결과 |
|---|---|
| `customerId` | `customerNumber` 권장, `WARNING` |
| `orderNumber` | issue 없음 |

## DDL Validator

DDL은 `CREATE TABLE (...)` 블록에서 컬럼 정의를 추출한다.

검증 항목:

- 컬럼명: `ExpressionType.DB_COLUMN`
- 물리 타입
- 자릿수
- 소수점

예:

| 입력 | 결과 |
|---|---|
| `CUST_ID VARCHAR(20)` | `CUST_NO` 권장, `ERROR` |
| `ORD_AMT NUMERIC(10, 0)` | 자릿수/소수점 불일치, `ERROR` |

## 제한

MVP 구현은 기본 SQL DDL과 OpenAPI schema property 검증에 집중한다.

## Code Validator

Kotlin, Java, TypeScript 산출물에서 DTO/Entity/Request/Response field를 추출한다.

| 산출물 | 추출 대상 | 검증 표현 유형 |
|---|---|---|
| Kotlin | `val customerId: String`, `var customerId: String` | `CODE_VARIABLE` |
| Java | `private String customerId;` | `CODE_VARIABLE` |
| TypeScript | `customerId: string` | `CODE_VARIABLE` |
| Kotlin/Java Entity | `@Column(name = "CUST_ID")` | `DB_COLUMN` |
| Kotlin/Java JSON | `@JsonProperty("customerId")` | `API_FIELD` |

`customerId`는 고객번호 alias로 탐지되어 `customerNumber` 권장 `WARNING`으로 반환된다.

## SQL Validator

SQL Mapper에서 대문자 DB 컬럼과 named parameter를 추출한다.

| 입력 | 검증 표현 유형 |
|---|---|
| `CUST_ID` | `DB_COLUMN` |
| `:customerId`, `#{customerId}` | `CODE_VARIABLE` |

`CUST_ID`는 금지어 alias라 `CUST_NO` 권장 `ERROR`로 반환된다.

## Test Term Validator

Gherkin, 테스트 display name, assertion message에서 한글 업무 용어와 코드 변수를 추출한다.

| 입력 | 검증 표현 유형 |
|---|---|
| `고객 ID` | `TEST_WORD` |
| `고객번호` | `TEST_WORD` |
| `customerId` | `CODE_VARIABLE` |

`고객 ID`는 `고객번호` 권장 `WARNING`으로 반환된다.

## 자동 수정 가능 여부

현재 API 응답 schema에는 별도 `fixable` 필드가 없다. 다음 기준으로 PR/CLI 단계에서 구분한다.

- `recommendedExpression`이 있고 단일 표현 치환이면 자동 수정 가능
- 물리 타입, 자릿수, 소수점 불일치 또는 신규 후보는 수동 검토

## 제한

MVP 구현은 정규식 기반 추출에 집중한다.

- 복잡한 DB vendor syntax는 후속 태스크에서 보강한다.
- `$ref` resolve는 현재 직접 수행하지 않는다.
- OpenAPI parameter name 검증은 후속 확장 대상이다.
- 복잡한 Kotlin/Java/TypeScript AST는 후속 태스크에서 tree-sitter 또는 compiler API로 교체한다.
