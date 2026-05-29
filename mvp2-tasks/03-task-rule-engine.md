# Task 03: Rule Engine 구현

## 목표

기획서, API, DB, 코드 산출물에서 표준 용어 사용 여부를 판단하는 Rule Engine을 구현한다.

## 범위

- 상태 기반 사용 가능 여부 검증
- 표현 유형별 표준 일치 검증
- 금지어 검증
- 폐기어 검증
- 검증 결과 표준 포맷

## 상세 태스크

1. `ValidationIssue` 도메인 모델을 구현한다.
2. severity 값을 `ERROR`, `WARNING`, `INFO`로 정의한다.
3. Approved 용어 개발 사용 가능 규칙을 구현한다.
4. Draft 용어 개발 사용 금지 규칙을 구현한다.
5. Reviewing 용어 개발 사용 금지 규칙을 구현한다.
6. Deprecated 용어 사용 금지 규칙을 구현한다.
7. Rejected 용어 추천 금지 규칙을 구현한다.
8. 금지어 사용 시 권장 표현을 반환하는 규칙을 구현한다.
9. API 필드명 표준 일치 규칙을 구현한다.
10. DB 컬럼명 표준 일치 규칙을 구현한다.
11. 코드 변수명 표준 일치 규칙을 구현한다.
12. 물리 타입, 자릿수, 소수점 일치 규칙을 설계한다.
13. Rule Engine 단위 테스트를 작성한다.

## 산출물

- Rule Engine 코드
- 검증 규칙 목록
- 검증 결과 schema
- 단위 테스트

## 완료 기준

- `customerId` 입력 시 `customerNumber` 권장을 반환할 수 있다.
- Deprecated 용어 사용은 ERROR로 반환한다.
- Draft 용어 개발 산출물 사용은 ERROR로 반환한다.

