# Task 03: Term API 구현

## 목표

OpenAPI Spec을 기준으로 용어 관리 Backend API를 구현한다.

## 선행 조건

- `mvp1-tasks/02-task-term-openapi-spec.md` 완료
- Term API OpenAPI Spec 확정
- DB core schema 구현 완료

## 상세 태스크

1. OpenAPI Spec에서 서버 인터페이스 또는 DTO 타입을 생성한다.
2. 생성된 타입을 기준으로 Controller 또는 Route를 구현한다.
3. 용어 등록 API를 구현한다.
4. 용어 목록 조회 API를 구현한다.
5. 용어 상세 조회 API를 구현한다.
6. 용어 수정 API를 구현한다.
7. 용어 승인 API를 구현한다.
8. 용어 폐기 API를 구현한다.
9. 변경 이력 조회 API를 구현한다.
10. Expression 등록/조회 API를 구현한다.
11. Alias 등록/조회 API를 구현한다.
12. 요청 validation을 OpenAPI Spec과 일치시킨다.
13. 응답 schema가 OpenAPI Spec과 일치하는지 contract test를 작성한다.
14. API 구현 결과와 OpenAPI Spec 차이를 CI에서 검증할 수 있게 준비한다.

## 산출물

- Term API 구현 코드
- 생성 DTO 또는 서버 인터페이스
- API contract test
- 단위 테스트

## 완료 기준

- OpenAPI Spec에 정의된 Term API가 모두 동작한다.
- Spec과 다른 응답 필드가 발생하지 않는다.
- 고객번호 용어를 등록, 조회, 승인, 폐기할 수 있다.

