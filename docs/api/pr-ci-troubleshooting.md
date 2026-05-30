# PR/CI 대응 가이드

## 1. CI 실패 기준

| 조건 | 결과 |
|---|---|
| `ERROR` 존재 | CI 실패 |
| `WARNING`만 존재 | 기본 CI 통과 |
| `--fail-on warning` 사용 | WARNING도 CI 실패 |
| `INFO`만 존재 | CI 통과, 후보 검토 |

## 2. 자주 나오는 위반

| 입력 | 원인 | 조치 |
|---|---|---|
| `customerId` | 고객번호 코드 표준 불일치 | `customerNumber`로 변경 |
| `CUST_ID` | Forbidden DB 컬럼 | `CUST_NO`로 변경 |
| `고객 ID` | 테스트/문서 표현 비표준 | `고객번호`로 변경 |
| 새 업무 용어 | 데이터 사전 미등록 | 신규 후보 등록 |

## 3. 수정 절차

1. PR 코멘트의 `Severity`를 확인한다.
2. `ERROR`를 먼저 수정한다.
3. `Recommendation` 값으로 이름을 교체한다.
4. OpenAPI를 수정했다면 generator를 다시 실행한다.
5. 백엔드/프런트 테스트를 실행한다.
6. 로컬 CLI를 다시 실행한다.

## 4. OpenAPI 수정 후 명령

```bash
openapi-generator-cli validate -i openapi/openapi.yaml
./scripts/openapi-generate-backend.sh
./scripts/openapi-generate-frontend.sh
```

## 5. 테스트 명령

```bash
cd apps/backend
gradle test
```

```bash
cd apps/frontend
npm run typecheck
```

## 6. 예외 처리

정말 기존 외부 계약 때문에 비표준 표현을 유지해야 하면 다음을 남긴다.

- 유지 이유
- 영향 API/DB/DTO
- 폐기 예정일
- 대체 표준 용어
- 데이터 스튜어드 승인자

임시 예외는 신규 표준으로 등록하지 않는다.
