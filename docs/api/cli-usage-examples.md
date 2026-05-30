# CLI 사용 예시

## 1. 변경 파일 검증

```bash
git diff --name-only origin/main...HEAD > changed-files.txt
ruby scripts/aulms-pr-review.rb --changed-files changed-files.txt
```

## 2. 리포트 경로 지정

```bash
ruby scripts/aulms-pr-review.rb \
  --changed-files changed-files.txt \
  --output-json build/reports/aulms-pr-review.json \
  --output-md build/reports/aulms-pr-review.md
```

## 3. WARNING도 실패 처리

```bash
ruby scripts/aulms-pr-review.rb \
  --changed-files changed-files.txt \
  --fail-on warning
```

## 4. 샘플 응답으로 리포트 생성

```bash
ruby scripts/aulms-pr-review.rb \
  --response-json scripts/fixtures/aulms-pr-review-response.json \
  --output-json build/reports/sample.json \
  --output-md build/reports/sample.md
```

## 5. E2E 테스트

```bash
ruby scripts/test-aulms-pr-review.rb
```

성공 출력:

```text
aulms-pr-review e2e ok
```
