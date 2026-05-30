# PR 리뷰 봇 및 CI 품질 게이트

## 목적

PR 변경 파일을 `/reviews/pr` API로 검증하고, 표준 위반을 Markdown 리포트와 PR 코멘트로 남긴다.

## 구성

| 파일 | 설명 |
|---|---|
| `scripts/aulms-pr-review.rb` | 변경 파일 수집, Review API 호출, JSON/Markdown 리포트 생성 |
| `.github/workflows/aulms-artifact-review.yml` | GitHub PR 검증 workflow |
| `scripts/test-aulms-pr-review.rb` | 샘플 응답 기반 E2E 테스트 |
| `scripts/fixtures/aulms-pr-review-response.json` | E2E 테스트 fixture |

## 동작 흐름

```text
PR 생성/갱신
  -> changed-files.txt 생성
  -> scripts/aulms-pr-review.rb 실행
  -> POST /reviews/pr 호출
  -> JSON report 저장
  -> Markdown report 저장
  -> PR summary comment upsert
  -> ERROR 존재 시 CI 실패
```

## Severity 정책

| Severity | CI 처리 | PR 처리 |
|---|---|---|
| `ERROR` | 실패 | 코멘트 |
| `WARNING` | 통과 | 코멘트 |
| `INFO` | 통과 | summary |

`--fail-on warning`을 사용하면 WARNING도 실패 처리할 수 있다.

## 중복 코멘트 방지

Markdown 리포트는 `<!-- aulms-artifact-review -->` marker를 포함한다.

GitHub workflow는 기존 봇 코멘트 중 marker가 있는 코멘트를 찾아 update하고, 없으면 create한다.

## 실행 예시

```bash
git diff --name-only origin/main...HEAD > changed-files.txt
ruby scripts/aulms-pr-review.rb \
  --changed-files changed-files.txt \
  --output-json build/reports/aulms-pr-review.json \
  --output-md build/reports/aulms-pr-review.md \
  --fail-on error
```

## 완료 기준 매핑

| 완료 기준 | 구현 |
|---|---|
| PR에서 `customerId` 신규 사용 탐지 | `/reviews/pr` 응답 issue를 Markdown으로 변환 |
| `customerNumber` 권장 코멘트 | `recommendedExpression` 표시 |
| ERROR 위반 시 CI 실패 | `exitCode != 0`이면 workflow 마지막 step 실패 |
