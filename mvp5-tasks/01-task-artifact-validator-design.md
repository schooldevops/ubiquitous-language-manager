# Task 01: 개발 산출물 검증기 설계

## 목표

DDL, OpenAPI, DTO, Entity, SQL, 테스트, 화면 라벨을 검증하는 공통 구조를 설계한다.

## 범위

- 파일 유형별 parser 전략
- 검증 결과 표준 포맷
- Rule Engine 연동
- CLI 실행 방식
- PR 봇 연동 방식

## 상세 태스크

1. DDL parser 전략을 정의한다.
2. OpenAPI YAML parser 전략을 정의한다.
3. Kotlin/Java/TypeScript DTO parser 전략을 정의한다.
4. SQL Mapper parser 전략을 정의한다.
5. 테스트 코드 용어 추출 전략을 정의한다.
6. 화면 라벨 추출 전략을 정의한다.
7. 검증 결과 공통 모델을 정의한다.
8. Rule Engine 연동 인터페이스를 정의한다.
9. CLI 입력/출력 형식을 정의한다.
10. PR 리뷰 코멘트 변환 규칙을 정의한다.

## 산출물

- 검증기 설계 문서
- parser 전략 문서
- CLI 입출력 형식
- PR 코멘트 포맷

## 완료 기준

- 각 파일 유형을 어떤 방식으로 검증할지 구현자가 이해할 수 있다.
- 검증 결과가 Rule Engine 결과 형식과 호환된다.

