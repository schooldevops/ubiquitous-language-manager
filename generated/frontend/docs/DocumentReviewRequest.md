# DocumentReviewRequest

기획서 본문 용어 검토 요청

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**documentText** | **string** | 검토할 기획서 본문 | [default to undefined]
**domainNames** | **Array&lt;string&gt;** | 검토 대상 도메인 필터 | [optional] [default to undefined]
**_options** | [**DocumentReviewOptions**](DocumentReviewOptions.md) |  | [optional] [default to undefined]

## Example

```typescript
import { DocumentReviewRequest } from '@aulms/api-client';

const instance: DocumentReviewRequest = {
    documentText,
    domainNames,
    _options,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
