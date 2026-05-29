# Task 04: 개발 산출물 Review API 구현

## 목표

외부 도구, PR 봇, CI에서 검증기를 호출할 수 있도록 Review API를 제공한다.

## OpenAPI 필수 조건

서버 구현 전에 다음 API를 OpenAPI Spec에 먼저 정의해야 한다.

- `POST /reviews/openapi`
- `POST /reviews/ddl`
- `POST /reviews/code`
- `POST /reviews/pr`

## 상세 태스크

1. OpenAPI 검증 요청/응답 schema를 정의한다.
2. DDL 검증 요청/응답 schema를 정의한다.
3. 코드 검증 요청/응답 schema를 정의한다.
4. PR 검증 요청/응답 schema를 정의한다.
5. 파일 내용, 파일 경로, 검증 옵션을 요청에 포함한다.
6. 검증 결과는 `ValidationIssue` 배열로 반환한다.
7. OpenAPI Spec에서 서버 타입을 생성한다.
8. OpenAPI 검증 API를 구현한다.
9. DDL 검증 API를 구현한다.
10. 코드 검증 API를 구현한다.
11. PR 검증 API를 구현한다.
12. contract test를 작성한다.

## 산출물

- Review API OpenAPI Spec
- 산출물 검증 API
- contract test

## 완료 기준

- CI 또는 PR 봇이 HTTP API로 검증을 요청할 수 있다.
- 응답은 파일 위치, 위반 사유, 권장 표현을 포함한다.

