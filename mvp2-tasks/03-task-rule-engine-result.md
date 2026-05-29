# Task 03 Result: Rule Engine 구현

## 1. 수행 범위

- `ValidationIssue` 생성 모델 기반 검증 결과 사용
- `ERROR`, `WARNING`, `INFO` severity 적용
- 상태 기반 개발 산출물 사용 가능 여부 규칙 구현
- 금지어, 폐기어, 유사어 검증 규칙 구현
- API 필드명, DB 컬럼명, 코드 변수명 표준 일치 규칙 구현
- 물리 타입, 자릿수, 소수점 일치 규칙 구현
- Rule Engine 단위 테스트 작성

## 2. 변경 파일

| 파일 | 변경 내용 |
|---|---|
| `apps/backend/src/main/kotlin/com/aulms/rule/RuleEngine.kt` | Rule Engine과 검증 요청 모델 구현 |
| `apps/backend/src/test/kotlin/com/aulms/rule/RuleEngineTest.kt` | Rule Engine 단위 테스트 추가 |
| `docs/api/rule-engine.md` | 검증 규칙 문서 추가 |

## 3. 완료 기준 대응

| 완료 기준 | 결과 |
|---|---|
| `customerId` 입력 시 `customerNumber` 권장 | 구현 |
| Deprecated 용어 사용은 `ERROR` 반환 | 구현 |
| Draft 용어 개발 산출물 사용은 `ERROR` 반환 | 구현 |

## 4. 검증

- `gradle test` 통과
