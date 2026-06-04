# TermUploadResult


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**uploadBatchId** | **string** |  | [default to undefined]
**uploadedAt** | **string** |  | [default to undefined]
**totalRows** | **number** |  | [default to undefined]
**inserted** | **number** |  | [default to undefined]
**updated** | **number** |  | [default to undefined]
**failed** | **number** |  | [default to undefined]
**rows** | [**Array&lt;TermUploadRow&gt;**](TermUploadRow.md) |  | [default to undefined]

## Example

```typescript
import { TermUploadResult } from '@aulms/api-client';

const instance: TermUploadResult = {
    uploadBatchId,
    uploadedAt,
    totalRows,
    inserted,
    updated,
    failed,
    rows,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
