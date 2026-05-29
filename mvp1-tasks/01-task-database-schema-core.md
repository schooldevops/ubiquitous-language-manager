# Task 01: 데이터 사전 코어 DB 스키마 구현

## 목표

표준 용어 관리에 필요한 핵심 데이터베이스 스키마를 구현한다.

## 범위

- `TERM_MASTER`
- `TERM_EXPRESSION`
- `TERM_ALIAS`
- `TERM_RELATIONSHIP`
- `TERM_CHANGE_HISTORY`
- `TERM_CANDIDATE`
- `TERM_REVIEW_REQUEST`

## 상세 태스크

1. `TERM_MASTER` 테이블을 설계하고 생성한다.
2. `TERM_EXPRESSION` 테이블을 설계하고 생성한다.
3. `TERM_ALIAS` 테이블을 설계하고 생성한다.
4. `TERM_RELATIONSHIP` 테이블을 설계하고 생성한다.
5. `TERM_CHANGE_HISTORY` 테이블을 설계하고 생성한다.
6. `TERM_CANDIDATE` 테이블을 설계하고 생성한다.
7. `TERM_REVIEW_REQUEST` 테이블을 설계하고 생성한다.
8. 상태값 enum 또는 check constraint를 적용한다.
9. 표현 유형 enum 또는 check constraint를 적용한다.
10. 별칭 유형 enum 또는 check constraint를 적용한다.
11. 검색 성능을 위한 인덱스를 설계한다.
12. 샘플 데이터 seed 스크립트를 작성한다.

## 산출물

- DB migration 파일
- seed 데이터
- ERD 또는 테이블 설명 문서

## 완료 기준

- 고객번호 샘플 용어를 DB에 저장할 수 있다.
- 고객번호의 DB 컬럼명, API 필드명, 코드 변수명을 표현 매핑으로 저장할 수 있다.
- 고객ID를 고객번호의 유사어로 저장할 수 있다.

