# PullRequestArtifactFile

PR 검증 대상 파일

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**filePath** | **string** | 변경 파일 경로 | [default to undefined]
**content** | **string** | 변경 파일 본문 | [default to undefined]
**sourceType** | [**ArtifactSourceType**](ArtifactSourceType.md) |  | [optional] [default to undefined]

## Example

```typescript
import { PullRequestArtifactFile } from '@aulms/api-client';

const instance: PullRequestArtifactFile = {
    filePath,
    content,
    sourceType,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
