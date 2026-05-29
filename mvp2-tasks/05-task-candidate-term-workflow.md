# Task 05: 신규 용어 후보 워크플로우 구현

## 목표

데이터 사전에 없는 용어를 신규 후보로 등록하고 검토할 수 있는 흐름을 구현한다.

## OpenAPI 필수 조건

Candidate API는 구현 전에 OpenAPI Spec에 먼저 정의해야 한다.

## 상세 태스크

1. `POST /candidates` OpenAPI Spec을 작성한다.
2. `GET /candidates` OpenAPI Spec을 작성한다.
3. `GET /candidates/{candidateId}` OpenAPI Spec을 작성한다.
4. `POST /candidates/{candidateId}/review` OpenAPI Spec을 작성한다.
5. `POST /candidates/{candidateId}/promote` OpenAPI Spec을 작성한다.
6. 신규 후보 등록 API를 구현한다.
7. 후보 목록 조회 API를 구현한다.
8. 후보 상세 조회 API를 구현한다.
9. 후보 검토 API를 구현한다.
10. 후보를 표준 용어로 승격하는 API를 구현한다.
11. 후보 등록 시 기존 유사 용어 검색을 수행한다.
12. 후보 상태 변경 이력을 기록한다.
13. 후보 UI 화면을 구현한다.

## 산출물

- Candidate API OpenAPI Spec
- 신규 용어 후보 API
- 후보 검토 화면
- 후보 승격 로직

## 완료 기준

- 미등록 용어가 검토 결과에서 신규 후보로 생성될 수 있다.
- 후보를 승인하면 `TERM_MASTER`와 `TERM_EXPRESSION`에 반영할 수 있다.

