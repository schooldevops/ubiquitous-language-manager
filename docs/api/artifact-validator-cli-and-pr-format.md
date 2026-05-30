# Artifact Validator CLI 및 PR 포맷

## 1. CLI 명령

### 단일 파일 검증

```bash
aulms-validator validate --type ddl --file schema/order.sql
```

### 변경 파일 검증

```bash
aulms-validator validate --changed-files changed-files.txt --format json
```

### 표준 입력 검증

```bash
aulms-validator validate --type openapi --stdin
```

### CI 모드

```bash
aulms-validator validate --changed-files changed-files.txt --ci --fail-on error
```

## 2. CLI 옵션

| 옵션 | 설명 | 기본값 |
|---|---|---|
| `--type` | 파일 유형. `auto`, `ddl`, `openapi`, `kotlin`, `java`, `typescript`, `sql`, `test`, `ui-label` | `auto` |
| `--file` | 단일 파일 경로 | 없음 |
| `--changed-files` | 검증할 파일 목록 | 없음 |
| `--stdin` | 표준 입력 사용 | `false` |
| `--format` | `json`, `markdown`, `sarif` | `json` |
| `--domain` | 도메인 필터 | 전체 |
| `--fail-on` | `none`, `warning`, `error` | `error` |
| `--include-suggestions` | 권장 표현 포함 | `true` |

## 3. JSON 출력

```json
{
  "summary": {
    "files": 1,
    "checkedCount": 5,
    "errorCount": 1,
    "warningCount": 1,
    "infoCount": 0
  },
  "results": [
    {
      "filePath": "CustomerOrderResponse.kt",
      "sourceType": "KOTLIN",
      "checkedCount": 3,
      "issues": [
        {
          "severity": "WARNING",
          "source": "KOTLIN",
          "location": "CustomerOrderResponse.kt:12",
          "inputExpression": "customerId",
          "standardTerm": "고객번호",
          "recommendedExpression": "customerNumber",
          "reason": "'고객번호'의 표준 코드 변수명은 customerNumber입니다."
        }
      ]
    }
  ],
  "exitCode": 1
}
```

## 4. Markdown 출력

```markdown
# 데이터 사전 표준 검증 결과

| Severity | Count |
|---|---:|
| ERROR | 1 |
| WARNING | 1 |
| INFO | 0 |

## CustomerOrderResponse.kt

| Line | Severity | Input | Recommendation | Reason |
|---:|---|---|---|---|
| 12 | WARNING | customerId | customerNumber | 고객번호 표준 코드 변수명 불일치 |
```

## 5. PR Inline Comment

```text
[데이터 사전 표준 위반]

현재 표현:
customerId

권장 표현:
customerNumber

표준 용어:
고객번호

사유:
'고객번호'의 표준 API/코드 표현은 customerNumber입니다.

심각도:
WARNING
```

## 6. PR Summary Comment

```markdown
## 데이터 사전 표준 검증 결과

| Severity | Count |
|---|---:|
| ERROR | 1 |
| WARNING | 3 |
| INFO | 2 |

ERROR가 있어 CI 품질 게이트를 통과할 수 없습니다.

### 주요 위반

| File | Line | Input | Recommendation |
|---|---:|---|---|
| schema/customer.sql | 8 | CUST_ID | CUST_NO |
| CustomerOrderResponse.kt | 12 | customerId | customerNumber |
```

## 7. SARIF 매핑

| ValidationIssue | SARIF |
|---|---|
| `severity=ERROR` | `level=error` |
| `severity=WARNING` | `level=warning` |
| `severity=INFO` | `level=note` |
| `location` | `physicalLocation.region.startLine` |
| `reason` | `message.text` |
| `recommendedExpression` | `fixes.artifactChanges.replacements` |

## 8. 종료 코드

| 조건 | Exit code |
|---|---:|
| 이슈 없음 | 0 |
| `--fail-on none` | 0 |
| fail 기준 이상 이슈 존재 | 1 |
| parser 오류 | 2 |
| 설정 오류 | 3 |
| 데이터 사전 조회 실패 | 4 |
