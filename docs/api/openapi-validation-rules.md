# OpenAPI 검증 규칙

## 1. 기본 규칙

- 모든 API는 `openapi/openapi.yaml` 또는 참조된 path 파일에 정의되어야 한다.
- 모든 요청/응답 schema는 `description`을 가져야 한다.
- 모든 enum은 허용 값을 명시해야 한다.
- 모든 오류 응답은 `ErrorResponse`를 사용해야 한다.
- 목록 응답은 `PageMetadata`를 포함해야 한다.
- 보호 API는 `security`와 `x-required-roles`를 명시해야 한다.
- API 필드명은 데이터 딕셔너리의 표준 `API_FIELD` 표현을 사용해야 한다.
- Deprecated 용어와 Forbidden 별칭은 API schema 이름이나 필드명으로 사용할 수 없다.

## 2. HTTP 응답 규칙

| 상태 코드 | 사용 기준 |
|---|---|
| 200 | 조회, 검색, 검토 성공 |
| 201 | 리소스 생성 성공 |
| 204 | 본문 없는 상태 변경 성공 |
| 400 | 요청 형식 오류 |
| 401 | 인증 필요 |
| 403 | 권한 없음 |
| 404 | 리소스 없음 |
| 409 | 중복 또는 상태 충돌 |
| 422 | 업무 규칙 위반 |
| 500 | 서버 오류 |

## 3. Contract 검증 규칙

Backend 구현 후 다음 항목을 검증한다.

- OpenAPI Spec에서 server stub 생성이 성공해야 한다.
- OpenAPI Spec에서 TypeScript axios client 생성이 성공해야 한다.
- Controller 또는 API 구현체는 생성된 interface를 구현해야 한다.
- API 응답 필드는 OpenAPI schema와 일치해야 한다.
- validation 오류는 `ErrorResponse`로 반환해야 한다.
- contract test는 OpenAPI 예시와 실제 응답을 비교해야 한다.

## 4. 권한 검증 규칙

OpenAPI Spec에서 보호 API는 다음 확장 필드를 사용한다.

```yaml
x-required-roles:
  - ADMIN
  - DATA_STEWARD
```

권한이 도메인 소유권에 따라 달라지는 API는 다음 확장 필드를 추가한다.

```yaml
x-domain-scope: true
```

## 5. 생성 명령 기준

실제 스크립트는 Backend/Frontend scaffold 단계에서 확정한다. 기준 명령은 다음과 같다.

```bash
openapi-generator-cli generate \
  -g spring \
  -i openapi/openapi.yaml \
  -o generated/backend

openapi-generator-cli generate \
  -g typescript-axios \
  -i openapi/openapi.yaml \
  -o generated/frontend
```

