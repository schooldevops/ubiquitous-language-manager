# ArtifactValidationResult

개발 산출물 표준 용어 검증 결과

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**filePath** | **string** | 검증 대상 파일 경로 | [default to undefined]
**sourceType** | [**ArtifactSourceType**](ArtifactSourceType.md) |  | [default to undefined]
**checkedCount** | **number** | 검증한 표현 수 | [default to undefined]
**summary** | [**ArtifactValidationSummary**](ArtifactValidationSummary.md) |  | [default to undefined]
**issues** | [**Array&lt;ValidationIssue&gt;**](ValidationIssue.md) | 표준 위반 또는 권고 사항 | [default to undefined]
**exitCode** | **number** | CLI 호환 종료 코드. ERROR 또는 failOnWarning 조건이면 1. | [default to undefined]

## Example

```typescript
import { ArtifactValidationResult } from '@aulms/api-client';

const instance: ArtifactValidationResult = {
    filePath,
    sourceType,
    checkedCount,
    summary,
    issues,
    exitCode,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
