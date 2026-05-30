# 개발 산출물 검증기 설계

## 1. 목적

개발 산출물 검증기는 DDL, OpenAPI, DTO, Entity, SQL, 테스트, 화면 라벨에서 사내 표준 용어 위반을 추출하고 `RuleEngine` 검증 결과로 변환한다.

검증기는 다음 흐름을 따른다.

```text
파일 입력
  -> 파일 유형 판별
  -> Parser 선택
  -> 검증 대상 표현 추출
  -> Rule Engine 검증
  -> ValidationIssue 표준화
  -> CLI JSON / PR 코멘트 / CI 리포트 출력
```

## 2. 검증 대상

| 산출물 | 주요 검증 대상 | 표준 표현 유형 |
|---|---|---|
| DDL | 테이블명, 컬럼명, 타입, 길이, 소수점 | `DB_COLUMN` |
| OpenAPI YAML | schema property, request/response field, enum, description | `API_FIELD` |
| Kotlin/Java DTO | data class property, field, getter/setter | `CODE_VARIABLE` |
| TypeScript DTO | interface property, type alias property | `CODE_VARIABLE` |
| Entity | DB 매핑 컬럼명, 엔티티 필드명 | `DB_COLUMN`, `CODE_VARIABLE` |
| SQL Mapper | SELECT/INSERT/UPDATE 컬럼, parameter alias | `DB_COLUMN`, `CODE_VARIABLE` |
| 테스트 코드 | Given/When/Then 문장, assertion message, display name | `TEST_WORD` |
| 화면 라벨 | JSX text, i18n key value, form label | `UI_LABEL` |

## 3. 공통 모델

### ArtifactValidationRequest

| 필드 | 설명 |
|---|---|
| `sourceType` | `DDL`, `OPENAPI`, `KOTLIN`, `JAVA`, `TYPESCRIPT`, `SQL`, `TEST`, `UI_LABEL`, `AUTO` |
| `filePath` | 파일 경로 |
| `content` | 검증 대상 원문 |
| `domainNames` | 도메인 필터 |
| `failOnWarning` | WARNING도 실패로 볼지 여부 |
| `includeSuggestions` | 권장 표현 포함 여부 |

### ExtractedExpression

| 필드 | 설명 |
|---|---|
| `expression` | 산출물에서 추출한 표현 |
| `expressionType` | 검증할 표준 표현 유형 |
| `location` | 파일, 라인, 컬럼, JSON pointer 등 위치 |
| `context` | 주변 코드나 문장 |
| `metadata` | 타입, 길이, 소수점, schema type 등 부가정보 |

### ArtifactValidationResult

| 필드 | 설명 |
|---|---|
| `filePath` | 검증 파일 |
| `sourceType` | 파일 유형 |
| `checkedCount` | 검증 표현 수 |
| `issues` | `ValidationIssue` 목록 |
| `summary` | severity별 집계 |
| `exitCode` | CLI 종료 코드 |

## 4. Rule Engine 연동

검증기는 parser 결과를 `RuleEngine.validateExpression()` 입력으로 변환한다.

| Parser 결과 | Rule Engine 입력 |
|---|---|
| `expression` | 입력 표현 |
| `expressionType` | `DB_COLUMN`, `API_FIELD`, `CODE_VARIABLE`, `UI_LABEL`, `TEST_WORD` |
| `metadata.physicalType` | 물리 타입 검증 |
| `metadata.digits` | 자릿수 검증 |
| `metadata.decimalPoint` | 소수점 검증 |
| `sourceType` + `location` | `ValidationIssue.source`, `location` |

## 5. Severity 정책

| 조건 | Severity | CI 기본 처리 |
|---|---|---|
| Forbidden 표현 신규 사용 | `ERROR` | 실패 |
| Deprecated 용어 신규 사용 | `ERROR` | 실패 |
| Draft/Reviewing 용어 개발 산출물 사용 | `ERROR` | 실패 |
| 표준 표현과 불일치 | `WARNING` | 통과, 리포트 |
| 의미 후보만 존재 | `INFO` | 통과 |
| 물리 타입, 자릿수, 소수점 불일치 | `ERROR` | 실패 |

## 6. Parser 인터페이스

```kotlin
interface ArtifactParser {
    fun supports(sourceType: ArtifactSourceType, filePath: String): Boolean
    fun parse(request: ArtifactParseRequest): List<ExtractedExpression>
}
```

```kotlin
data class ArtifactParseRequest(
    val sourceType: ArtifactSourceType,
    val filePath: String,
    val content: String,
)
```

Parser는 표준 검증을 직접 하지 않는다. Parser는 표현과 위치만 추출하고, 판단은 Rule Engine이 담당한다.

## 7. 실행 방식

### 단일 파일

```bash
aulms-validator validate --type openapi --file openapi/openapi.yaml --format json
```

### 여러 파일

```bash
aulms-validator validate --changed-files changed-files.txt --format markdown
```

### CI 모드

```bash
aulms-validator validate --changed-files changed-files.txt --ci --fail-on error
```

## 8. PR 봇 연동

PR 봇은 JSON 결과를 받아 파일별 inline comment와 summary comment로 변환한다.

- inline comment: 파일과 라인이 있는 `ERROR`, `WARNING`
- summary comment: 전체 집계, 신규 후보, 라인 미상 이슈
- resolved 처리: 같은 파일, 같은 라인, 같은 입력 표현, 같은 권장 표현이면 동일 이슈로 본다.

## 9. 확장 원칙

- parser 추가는 `ArtifactParser` 구현체 추가로 끝나야 한다.
- Rule Engine 결과 형식은 OpenAPI `ValidationIssue`와 호환해야 한다.
- 서버 API는 다음 태스크부터 반드시 OpenAPI spec 먼저 정의한다.
- CLI 출력은 서버 API 응답과 같은 모델을 사용한다.
