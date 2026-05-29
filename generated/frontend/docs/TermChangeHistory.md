# TermChangeHistory

용어 변경 이력

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**changeHistoryId** | **number** |  | [default to undefined]
**termId** | **string** |  | [optional] [default to undefined]
**changeType** | **string** |  | [default to undefined]
**previousStatus** | [**TermStatus**](TermStatus.md) |  | [optional] [default to undefined]
**newStatus** | [**TermStatus**](TermStatus.md) |  | [optional] [default to undefined]
**reason** | **string** |  | [default to undefined]
**requestedBy** | **string** |  | [optional] [default to undefined]
**approvedBy** | **string** |  | [optional] [default to undefined]
**impactAnalysisId** | **string** |  | [optional] [default to undefined]
**createdAt** | **string** |  | [default to undefined]

## Example

```typescript
import { TermChangeHistory } from '@aulms/api-client';

const instance: TermChangeHistory = {
    changeHistoryId,
    termId,
    changeType,
    previousStatus,
    newStatus,
    reason,
    requestedBy,
    approvedBy,
    impactAnalysisId,
    createdAt,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
