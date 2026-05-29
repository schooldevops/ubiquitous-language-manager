# OpenAPI Baseline

## 1. 목적

본 문서는 AULMS 서버 API 개발의 기준이 되는 OpenAPI Baseline을 정의한다.

## 2. 버전 결정

| 항목 | 결정 |
|---|---|
| OpenAPI 버전 | 3.0.3 |
| 결정 사유 | Spring Boot Kotlin server stub과 TypeScript axios client 생성을 안정적으로 지원하기 위함 |
| 루트 파일 | `openapi/openapi.yaml` |
| 공통 schema | `openapi/schemas/common.yaml` |
| enum schema | `openapi/schemas/enums.yaml` |
| path 분할 위치 | `openapi/paths/*.yaml` |

## 3. API 태그 구조

| 태그 | 설명 |
|---|---|
| Term | 표준 용어 등록, 조회, 수정, 승인, 폐기 API |
| Expression | DB 컬럼명, API 필드명, 코드 변수명 등 표현 매핑 API |
| Alias | 유사어, 별칭, 금지어 관리 API |
| Search | 정확 검색, 유사어 검색, 의미 기반 검색 API |
| Review | 기획서, OpenAPI, DDL, 코드, PR 검증 API |
| Candidate | 신규 용어 후보 등록, 검토, 승격 API |
| Governance | 승인, 폐기, 정책, 권한 관련 API |
| Relationship | 용어 관계와 Graphify 관계 검색 API |
| Impact | 용어 변경 영향도 분석 API |

## 4. 공통 Schema

OpenAPI Baseline은 다음 공통 schema를 제공한다.

- `ErrorResponse`
- `PageMetadata`
- `ValidationIssue`
- `ValidationSeverity`
- `TermStatus`
- `ExpressionType`
- `AliasType`
- `RelationshipType`
- `UserRole`

## 5. 인증 및 권한 Placeholder

MVP Baseline에서는 `bearerAuth` security scheme을 placeholder로 정의한다. 실제 인증 구현 전에도 보호 API는 OpenAPI Spec에 security requirement와 권한 확장 필드를 명시해야 한다.

예시:

```yaml
security:
  - bearerAuth: []
x-required-roles:
  - ADMIN
  - DATA_STEWARD
```

## 6. Spec 분할 규칙

- 루트 문서는 `openapi/openapi.yaml`이다.
- 도메인별 API path는 `openapi/paths` 아래에 둔다.
- 재사용 schema는 `openapi/schemas` 아래에 둔다.
- path 파일은 태그 단위로 분리한다.
- schema 파일은 common, enums, term, search, review, candidate처럼 도메인 단위로 분리한다.

## 7. 서버 구현 원칙

서버 구현은 반드시 다음 순서를 따른다.

1. OpenAPI Spec 작성
2. OpenAPI lint
3. Spring Boot Kotlin server stub 생성
4. 생성된 interface와 DTO 기준 구현
5. contract test 작성
6. Spec과 구현 불일치 검증

## 8. 프론트엔드 구현 원칙

프론트엔드는 반드시 OpenAPI Generator로 생성한 axios TypeScript client를 사용한다.

1. OpenAPI Spec 작성
2. TypeScript axios client 생성
3. 생성 client를 화면에서 호출
4. 직접 axios request shape 작성 금지

