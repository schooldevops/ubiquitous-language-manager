# Task 02: Term API OpenAPI Spec 작성

## 목표

용어 등록, 조회, 수정, 승인, 폐기를 위한 서버 API를 OpenAPI Spec으로 먼저 정의한다.

## 범위

- Term API
- Expression API
- Alias API
- History API
- Approval API

## 상세 태스크

1. `POST /terms` 요청/응답을 OpenAPI Spec에 정의한다.
2. `GET /terms` 요청 파라미터와 응답을 정의한다.
3. `GET /terms/{termId}` 응답 스키마를 정의한다.
4. `PUT /terms/{termId}` 요청/응답을 정의한다.
5. `POST /terms/{termId}/approve` 요청/응답을 정의한다.
6. `POST /terms/{termId}/deprecate` 요청/응답을 정의한다.
7. `GET /terms/{termId}/history` 응답을 정의한다.
8. `POST /terms/{termId}/expressions`를 정의한다.
9. `GET /terms/{termId}/expressions`를 정의한다.
10. `POST /terms/{termId}/aliases`를 정의한다.
11. `GET /terms/{termId}/aliases`를 정의한다.
12. 요청 validation 규칙을 OpenAPI Schema에 명시한다.
13. 에러 응답을 공통 `ErrorResponse`로 연결한다.
14. OpenAPI lint를 통과시킨다.

## 산출물

- Term API OpenAPI Spec
- Expression API OpenAPI Spec
- Alias API OpenAPI Spec
- API 예시 요청/응답

## 완료 기준

- Backend 구현, 자가 Spec만 보고 API를 구현할 수 있다.
- 서버 코드 생성 또는 타입 생성에 사용할 수 있는 수준의 OpenAPI Spec이 준비되어 있다.

