# Artifact Parser 전략

## 1. DDL Parser

DDL은 SQL parser 기반으로 처리한다. 정규식은 보조 수단으로만 사용한다.

| 추출 대상 | 방법 | 메타데이터 |
|---|---|---|
| `CREATE TABLE` 테이블명 | AST table node | schema, table |
| 컬럼명 | column definition node | column |
| 타입 | data type node | physicalType |
| 길이 | type parameter | digits |
| 소수점 | type parameter | decimalPoint |

검증 규칙:

- 컬럼명은 `DB_COLUMN` 표현과 일치해야 한다.
- `CUST_ID` 같은 Forbidden alias는 `ERROR`.
- 타입, 길이, 소수점은 `TERM_MASTER` 기준과 일치해야 한다.

## 2. OpenAPI YAML Parser

OpenAPI는 YAML parser로 AST를 만들고 JSON pointer 위치를 유지한다.

| 추출 대상 | 위치 예 |
|---|---|
| schema property | `/components/schemas/Customer/properties/customerNumber` |
| requestBody field | `/paths/~1orders/get/parameters/0/name` |
| response field | schema `$ref` resolve 후 property |
| enum value | `/components/schemas/Status/enum/0` |

검증 규칙:

- property name은 `API_FIELD` 표현과 일치해야 한다.
- schema description은 표준 용어 설명과 충돌하면 안 된다.
- Deprecated/Forbidden 표현은 `ERROR`.
- OpenAPI 검증 API는 이후 태스크에서 OpenAPI spec 우선 정의 후 구현한다.

## 3. Kotlin/Java DTO Parser

Kotlin/Java는 컴파일러 AST 또는 tree-sitter 기반 parser를 사용한다.

| 언어 | 추출 대상 |
|---|---|
| Kotlin | `data class` constructor property, class property, annotation value |
| Java | field, record component, getter/setter property |

검증 규칙:

- DTO/Request/Response field는 `CODE_VARIABLE` 표현과 일치해야 한다.
- `@JsonProperty`가 있으면 JSON field는 `API_FIELD`, 코드 필드는 `CODE_VARIABLE`로 각각 검증한다.
- `@Column(name = "...")`은 `DB_COLUMN`으로 검증한다.

## 4. TypeScript DTO Parser

TypeScript compiler API 또는 tree-sitter 기반 parser를 사용한다.

| 추출 대상 | 예 |
|---|---|
| interface property | `customerNumber: string` |
| type literal property | `{ customerNumber: string }` |
| zod schema key | `customerNumber: z.string()` |

검증 규칙:

- API client generated 파일은 기본적으로 제외한다.
- 수기 DTO와 화면 state field는 `CODE_VARIABLE`로 검증한다.
- API payload key는 `API_FIELD`로 검증한다.

## 5. SQL Mapper Parser

SQL Mapper는 SQL parser와 mapper 문법 parser를 조합한다.

| 대상 | 추출 방식 |
|---|---|
| SELECT 컬럼 | SQL AST projection |
| INSERT 컬럼 | SQL AST insert columns |
| UPDATE 컬럼 | SQL AST assignment |
| WHERE 컬럼 | SQL AST predicate |
| parameter | MyBatis `#{customerNumber}`, named parameter `:customerNumber` |

검증 규칙:

- DB 컬럼은 `DB_COLUMN`으로 검증한다.
- parameter alias는 `CODE_VARIABLE`로 검증한다.
- alias가 표준과 다르면 `WARNING`.

## 6. 테스트 코드 Parser

테스트 코드는 코드 AST와 자연어 추출을 함께 사용한다.

| 대상 | 추출 예 |
|---|---|
| Gherkin step | `Given 고객번호가 입력되지 않았을 때` |
| JUnit display name | `@DisplayName("고객번호 필수 검증")` |
| assertion message | `"고객번호는 필수입니다"` |
| 테스트 함수명 | `customerNumberRequired` |

검증 규칙:

- 한글 업무 용어는 `TEST_WORD`로 검증한다.
- 코드 변수는 `CODE_VARIABLE`로 검증한다.
- 오류 메시지의 화면 표시 용어는 `UI_LABEL` 또는 `TEST_WORD`로 검증한다.

## 7. 화면 라벨 Parser

화면 라벨은 JSX/TSX AST와 i18n 리소스를 대상으로 한다.

| 대상 | 추출 방식 |
|---|---|
| JSX text | `JSXText` |
| label prop | `<Field label="고객번호" />` |
| placeholder | `<input placeholder="고객번호" />` |
| i18n JSON | key-value value text |

검증 규칙:

- 화면 표시명은 `UI_LABEL`로 검증한다.
- 내부 key는 `CODE_VARIABLE`로 검증할 수 있다.
- 문장형 안내문은 용어 추출 후 부분 검증한다.

## 8. 제외 규칙

기본 제외 대상:

- `generated/**`
- `build/**`
- `.gradle/**`
- `node_modules/**`
- `.next/**`
- minified 파일
- vendor 파일

수동 제외:

```text
# aulms-validator-ignore next-line
val legacyCustomerId: String
```

ignore는 `reason`이 없으면 CI에서 `WARNING`으로 남긴다.
