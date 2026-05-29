# Task 04: 기획서 용어 검토 API 및 구현

## 목표

기획서 텍스트에서 비표준 용어를 탐지하고 표준 용어를 추천하는 Review API를 구현한다.

## OpenAPI 필수 조건

서버 API 구현 전에 반드시 `POST /reviews/document` OpenAPI Spec을 작성하고 승인한다.

## 상세 태스크

1. `POST /reviews/document` 요청 schema를 OpenAPI Spec에 정의한다.
2. 기획서 본문, 검토 옵션, 도메인 필터를 요청에 포함한다.
3. `DocumentReviewResult` 응답 schema를 정의한다.
4. `DetectedTerm`, `StandardMapping`, `CandidateTerm`, `ValidationIssue` schema를 정의한다.
5. OpenAPI 예시에 `고객 ID를 입력하면 주문 리스트를 조회한다.`를 포함한다.
6. OpenAPI Spec에서 서버 타입을 생성한다.
7. 문장 분리 로직을 구현한다.
8. 업무 용어 후보 추출 로직을 구현한다.
9. 정확 검색과 유사어 검색을 연결한다.
10. Rule Engine을 연결한다.
11. 표준화 문장 추천을 구현한다.
12. 용어 매핑표 생성을 구현한다.
13. 미매핑 표현은 신규 용어 후보로 반환한다.
14. contract test와 통합 테스트를 작성한다.

## 산출물

- Review API OpenAPI Spec
- 기획서 용어 검토 API
- 표준화 문장 추천 로직
- 용어 매핑표 생성 로직

## 완료 기준

- 입력 문장 `고객 ID를 입력하면 주문 리스트를 조회한다.`에 대해 `고객번호를 입력하면 주문 목록을 조회한다.`를 추천할 수 있다.
- 검토 결과에 표준 용어, DB 컬럼, API 필드, 사유가 포함된다.

