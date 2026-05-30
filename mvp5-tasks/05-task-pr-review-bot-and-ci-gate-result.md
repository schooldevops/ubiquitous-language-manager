# Task 05 결과: PR 리뷰 봇 및 CI 품질 게이트 구현

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, PR 변경 파일을 Review API로 검증하고 JSON/Markdown 리포트를 생성하는 CLI와 GitHub Actions workflow를 작성했다.

## 산출물

- `scripts/aulms-pr-review.rb`
- `scripts/test-aulms-pr-review.rb`
- `scripts/fixtures/aulms-pr-review-response.json`
- `.github/workflows/aulms-artifact-review.yml`
- `docs/api/pr-review-ci-gate.md`

## 구현 범위

- PR 변경 파일 목록 입력
- 파일 유형별 sourceType 지정
- `/reviews/pr` API 호출
- `ERROR`, `WARNING`, `INFO` severity 집계
- ERROR 존재 시 exit code 1 반환
- Markdown 리포트 생성
- JSON 리포트 생성
- PR summary comment upsert
- marker 기반 중복 코멘트 방지
- 샘플 응답 기반 E2E 테스트

## 완료 기준 확인

- PR에서 `customerId` 신규 사용을 탐지한 응답을 Markdown 코멘트로 변환한다.
- `customerNumber` 권장 표현을 리포트에 표시한다.
- `CUST_ID` 같은 ERROR 위반이 있으면 CLI exit code가 1이 된다.
- GitHub Actions workflow에서 exit code가 0이 아니면 CI를 실패시킨다.

## 검증 명령

```bash
ruby scripts/test-aulms-pr-review.rb
```

## 검증 결과

- 샘플 PR 응답 기반 E2E 테스트 성공
- Markdown report 생성 확인
- duplicate prevention marker 확인
- ERROR 응답 시 exit code 1 확인
