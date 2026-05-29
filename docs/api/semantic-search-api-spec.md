# Semantic Search API Spec

## 1. 목적

자연어 질의로 표준 용어 후보를 찾는 의미 기반 검색 API 계약을 정의한다.

## 2. 정의 API

| Method | Path | Operation ID | 설명 |
|---|---|---|---|
| POST | `/search/semantic` | `semanticSearch` | 자연어 의미 기반 용어 후보 검색 |

## 3. 요청 항목

- `query`: 자연어 질의문
- `domainNames`: 도메인 필터
- `statuses`: 상태 필터
- `limit`: 결과 개수

## 4. 대표 예시

`주문이 발생한 날짜` 질의는 `주문일자`와 `주문일시`를 후보로 반환한다.

| 후보 | 차이 |
|---|---|
| 주문일자 | 날짜만 필요할 때 사용 |
| 주문일시 | 시분초까지 필요할 때 사용 |
