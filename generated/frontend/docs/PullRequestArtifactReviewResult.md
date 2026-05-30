# PullRequestArtifactReviewResult

PR 변경 파일 표준 용어 검증 결과

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**pullRequestId** | **string** | PR 번호 또는 외부 시스템 PR 식별자 | [default to undefined]
**repository** | **string** | 저장소 이름 | [optional] [default to undefined]
**results** | [**Array&lt;ArtifactValidationResult&gt;**](ArtifactValidationResult.md) | 파일별 검증 결과 | [default to undefined]
**summary** | [**ArtifactValidationSummary**](ArtifactValidationSummary.md) |  | [default to undefined]
**issues** | [**Array&lt;ValidationIssue&gt;**](ValidationIssue.md) | 전체 파일의 표준 위반 또는 권고 사항 | [default to undefined]
**exitCode** | **number** | CLI/CI 호환 종료 코드 | [default to undefined]

## Example

```typescript
import { PullRequestArtifactReviewResult } from '@aulms/api-client';

const instance: PullRequestArtifactReviewResult = {
    pullRequestId,
    repository,
    results,
    summary,
    issues,
    exitCode,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
