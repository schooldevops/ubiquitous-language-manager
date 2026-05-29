# Task 05: PR 리뷰 봇 및 CI 품질 게이트 구현

## 목표

PR 변경 파일을 자동 검토하고 표준 위반을 코멘트하거나 CI에서 차단한다.

## 상세 태스크

1. PR 변경 파일 목록을 수집한다.
2. 파일 유형별 검증기를 선택한다.
3. Review API 또는 로컬 CLI로 검증을 실행한다.
4. ERROR, WARNING, INFO severity 정책을 적용한다.
5. ERROR가 있으면 CI를 실패시킨다.
6. WARNING은 CI를 통과시키되 리포트에 남긴다.
7. PR 코멘트 메시지를 생성한다.
8. 같은 위반에 대한 중복 코멘트를 방지한다.
9. Deprecated 용어 신규 사용을 ERROR로 처리한다.
10. Draft 용어 개발 산출물 사용을 ERROR로 처리한다.
11. JSON 및 Markdown 리포트를 생성한다.
12. 샘플 PR fixture로 E2E 테스트를 작성한다.

## 산출물

- PR 리뷰 봇
- CI Validator CLI
- Markdown 리포트
- JSON 리포트
- E2E 테스트

## 완료 기준

- PR에서 `customerId` 신규 사용을 탐지하고 `customerNumber`를 권장하는 코멘트를 작성할 수 있다.
- ERROR 위반이 있으면 CI가 실패한다.

