# API 요청/응답 공통 포맷

## 1. 공통 응답 원칙

API 응답은 리소스 중심으로 설계한다. 단건 조회는 리소스 객체를 직접 반환하고, 목록 조회는 페이지 메타데이터와 항목 목록을 포함한다.

## 2. 공통 에러 응답

```yaml
ErrorResponse:
  type: object
  required:
    - code
    - message
    - traceId
  properties:
    code:
      type: string
      description: 시스템에서 정의한 에러 코드
      example: TERM_NOT_FOUND
    message:
      type: string
      description: 사용자 또는 개발자가 이해할 수 있는 에러 메시지
      example: 표준 용어를 찾을 수 없습니다.
    detail:
      type: string
      description: 상세 에러 설명
      example: termId=T-000123
    traceId:
      type: string
      description: 요청 추적 ID
      example: 01HX8R7P9Q6RZC6Q9VQ0X7Z3WB
```

## 3. 페이지 응답

```yaml
PageResponse:
  type: object
  required:
    - page
    - size
    - totalElements
    - totalPages
  properties:
    page:
      type: integer
      minimum: 0
      description: 현재 페이지 번호
    size:
      type: integer
      minimum: 1
      description: 페이지 크기
    totalElements:
      type: integer
      minimum: 0
      description: 전체 항목 수
    totalPages:
      type: integer
      minimum: 0
      description: 전체 페이지 수
```

## 4. 검증 이슈 응답

```yaml
ValidationIssue:
  type: object
  required:
    - severity
    - source
    - inputExpression
    - reason
  properties:
    severity:
      type: string
      enum:
        - ERROR
        - WARNING
        - INFO
    source:
      type: string
      description: 문서, DDL, OpenAPI, 코드, PR 등 검증 출처
    location:
      type: string
      description: 파일명, 라인, 필드명 등 위치 정보
    inputExpression:
      type: string
      description: 입력 표현
    standardTerm:
      type: string
      description: 표준 용어
    recommendedExpression:
      type: string
      description: 권장 표현
    reason:
      type: string
      description: 위반 또는 권고 사유
```

## 5. HTTP 상태 코드

| 상태 코드 | 사용 기준 |
|---|---|
| 200 | 조회, 검토, 검색 성공 |
| 201 | 리소스 생성 성공 |
| 204 | 삭제 또는 상태 변경 성공이며 본문 없음 |
| 400 | 요청 형식 또는 validation 오류 |
| 401 | 인증 필요 |
| 403 | 권한 없음 |
| 404 | 리소스 없음 |
| 409 | 중복 용어, 중복 약어, 상태 충돌 |
| 422 | 업무 규칙 위반 |
| 500 | 서버 오류 |

