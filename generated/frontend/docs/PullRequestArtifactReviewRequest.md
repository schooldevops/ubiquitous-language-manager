# PullRequestArtifactReviewRequest

PR 변경 파일 표준 용어 검증 요청

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**pullRequestId** | **string** | PR 번호 또는 외부 시스템 PR 식별자 | [default to undefined]
**repository** | **string** | 저장소 이름 | [optional] [default to undefined]
**files** | [**Array&lt;PullRequestArtifactFile&gt;**](PullRequestArtifactFile.md) | 검증할 변경 파일 목록 | [default to undefined]
**failOnWarning** | **boolean** | WARNING도 실패 exitCode로 볼지 여부 | [optional] [default to false]
**includeSuggestions** | **boolean** | 권장 표현 포함 여부 | [optional] [default to true]

## Example

```typescript
import { PullRequestArtifactReviewRequest } from '@aulms/api-client';

const instance: PullRequestArtifactReviewRequest = {
    pullRequestId,
    repository,
    files,
    failOnWarning,
    includeSuggestions,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
