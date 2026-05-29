# Task 02: Graphify 동기화 Worker 구현

## 목표

관계형 DB의 용어 데이터를 Graphify 그래프로 동기화한다.

## 상세 태스크

1. `TERM_MASTER`에서 Term 노드를 생성한다.
2. 도메인 값에서 Domain 노드를 생성한다.
3. `TERM_EXPRESSION`에서 Column, APIField, DTOField 후보 노드를 생성한다.
4. `TERM_ALIAS`에서 synonym 또는 forbidden 관계를 생성한다.
5. `TERM_RELATIONSHIP`에서 용어 간 관계를 생성한다.
6. Deprecated 용어의 대체 관계를 생성한다.
7. 증분 동기화 기준을 구현한다.
8. 전체 재동기화 기능을 구현한다.
9. 동기화 실패 재시도 정책을 구현한다.
10. 동기화 결과 로그를 저장한다.
11. 샘플 데이터 동기화 테스트를 작성한다.

## 산출물

- Graphify Sync Worker
- 동기화 로그
- 동기화 테스트

## 완료 기준

- 고객번호 용어와 고객ID 별칭, CUST_NO 컬럼, customerNumber API 필드를 그래프에 동기화할 수 있다.

