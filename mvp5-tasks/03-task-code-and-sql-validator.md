# Task 03: 코드 및 SQL 검증기 구현

## 목표

DTO, Entity, Request/Response, SQL Mapper, 테스트 코드의 용어 사용을 검증한다.

## 상세 태스크

1. Kotlin data class 필드 추출을 구현한다.
2. Java DTO 필드 추출을 구현한다.
3. TypeScript interface/type 필드 추출을 구현한다.
4. Entity 속성 추출을 구현한다.
5. SQL Mapper 컬럼명 추출을 구현한다.
6. 테스트 코드의 한글 업무 용어 추출을 구현한다.
7. 코드 변수명을 `CODE_VARIABLE` 표현과 비교한다.
8. SQL 컬럼명을 `DB_COLUMN` 표현과 비교한다.
9. 테스트 용어를 `TEST_WORD` 표현과 비교한다.
10. 위반 위치 파일명과 라인 번호를 계산한다.
11. 자동 수정 가능한 항목과 수동 검토 항목을 구분한다.
12. 단위 테스트와 fixture를 작성한다.

## 산출물

- 코드 validator
- SQL validator
- 테스트 용어 validator
- 위치 기반 검증 리포트

## 완료 기준

- `val customerId: String`을 탐지하고 `val customerNumber: String`을 권장할 수 있다.
- 테스트 문장의 `고객 ID`를 탐지하고 `고객번호`를 권장할 수 있다.

