# Task 02: 정확 검색 및 유사어 검색 구현

## 목표

표준 용어, 표현 매핑, 별칭 데이터를 기반으로 정확 검색과 유사어 검색을 구현한다.

## 선행 조건

- Search API OpenAPI Spec 완료
- Term core schema 완료
- 샘플 데이터 적재 완료

## 상세 태스크

1. OpenAPI Spec에서 Search API 서버 인터페이스 또는 DTO를 생성한다.
2. 한글 용어명 exact match 검색을 구현한다.
3. 영문 용어명 exact match 검색을 구현한다.
4. 영문 약어 exact match 검색을 구현한다.
5. DB 컬럼명 exact match 검색을 구현한다.
6. API 필드명 exact match 검색을 구현한다.
7. 코드 변수명 exact match 검색을 구현한다.
8. 공백, 대소문자, 하이픈, 언더스코어 정규화 검색을 구현한다.
9. `TERM_ALIAS` 기반 유사어 검색을 구현한다.
10. 금지어 검색 시 표준 표현과 사유를 반환한다.
11. Deprecated 용어 검색 시 대체 용어를 반환한다.
12. 상태별 추천 정책을 적용한다.
13. OpenAPI contract test를 작성한다.

## 산출물

- Search API 구현
- 검색 정규화 유틸리티
- 검색 단위 테스트
- 검색 contract test

## 완료 기준

- `고객번호`, `CUST_NO`, `customerNumber`로 고객번호 용어를 찾을 수 있다.
- `고객ID` 검색 시 고객번호를 추천할 수 있다.
- `CUST_ID` 검색 시 금지어 사유와 `CUST_NO` 권장을 반환할 수 있다.

