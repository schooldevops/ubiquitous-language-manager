# API-first 개발 원칙

## 1. 기본 원칙

서버 API는 구현보다 OpenAPI Spec을 우선한다. 모든 Backend API는 OpenAPI Spec에 먼저 정의되어야 하며, 구현은 생성된 Spring Boot Kotlin server stub을 기준으로 진행한다.

## 2. 필수 절차

1. API 요구사항을 태스크 파일에서 확인한다.
2. 관련 유비쿼터스 언어와 데이터 딕셔너리를 확인한다.
3. OpenAPI path와 schema를 작성한다.
4. 요청, 응답, 에러, enum, 예시를 Spec에 포함한다.
5. OpenAPI lint를 실행한다.
6. OpenAPI Generator로 Backend server stub을 생성한다.
7. OpenAPI Generator로 Frontend axios TypeScript client를 생성한다.
8. 생성된 Backend server stub을 구현한다.
9. 생성된 Frontend client로 API를 호출한다.
10. contract test를 작성해 Spec과 구현의 일치 여부를 검증한다.

## 3. Backend 생성 규칙

| 항목 | 규칙 |
|---|---|
| generator | `spring` |
| language | Kotlin |
| framework | Spring Boot |
| source | `openapi/openapi.yaml` |
| output | `generated/backend` |
| 구현 위치 | `apps/backend` |

Backend 구현 시 생성된 DTO와 API interface를 임의로 우회하지 않는다.

## 4. Frontend 생성 규칙

| 항목 | 규칙 |
|---|---|
| generator | `typescript-axios` |
| source | `openapi/openapi.yaml` |
| output | `generated/frontend` |
| 사용 위치 | `apps/frontend` |

Frontend 구현 시 직접 axios request shape을 손으로 만들지 않고 생성 client를 우선 사용한다.

## 5. OpenAPI Spec 작성 규칙

- 모든 schema는 명확한 `description`을 가진다.
- enum은 가능한 값을 모두 명시한다.
- request body와 response body에는 예시를 포함한다.
- 에러 응답은 공통 `ErrorResponse`를 사용한다.
- 페이지 응답은 공통 `PageResponse` 형태를 따른다.
- API 필드명은 데이터 딕셔너리의 표준 API 필드명을 사용한다.
- Deprecated 용어 또는 금지어를 schema 이름과 필드명에 사용하지 않는다.

## 6. 검증 기준

- OpenAPI lint가 성공해야 한다.
- Backend server stub 생성이 성공해야 한다.
- Frontend axios TypeScript client 생성이 성공해야 한다.
- contract test가 성공해야 한다.
- 구현 코드의 요청/응답 필드는 Spec과 일치해야 한다.

