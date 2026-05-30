# Task 01 결과: Graphify 그래프 모델 설계

## 수행 내용

`rules.md`, `caveman.md`, Graphify skill 지침을 확인한 뒤, 용어 관계와 영향도 분석을 위한 그래프 모델 문서를 작성했다.

## 산출물

- `docs/graph/graph-model-design.md`

## 포함 내용

- 그래프 모델 목적과 설계 원칙
- 노드 정의표
  - `Term`
  - `Domain`
  - `System`
  - `Table`
  - `Column`
  - `API`
  - `APIField`
  - `DTO`
  - `Entity`
  - `Document`
  - `TestCase`
- 관계 정의표
  - `represents`
  - `mentions`
  - `uses`
  - `belongsTo`
  - `deprecatedBy`
- 관계 방향 기준
- 노드/관계 공통 속성
- Graphify 동기화 키
- 동기화 정책
- 삭제/폐기 데이터 처리 정책
- 고객번호 연결 예시
- Mermaid 예시
- Graphify 적재 단위

## 완료 기준 확인

다음 연결 모델을 정의했다.

```text
Column(CUST_NO) -> represents -> Term(고객번호)
APIField(customerNumber) -> represents -> Term(고객번호)
API(GET /orders) -> uses -> APIField(customerNumber)
Table(ORDER_HISTORY) -> uses -> Column(CUST_NO)
```

이 구조로 `고객번호`, `CUST_NO`, `customerNumber`, 관련 API 필드를 그래프로 연결할 수 있다.
