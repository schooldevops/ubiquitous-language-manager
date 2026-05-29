# SemanticSearchResponse

자연어 의미 기반 용어 검색 응답

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**query** | **string** | 입력 질의문 | [default to undefined]
**items** | [**Array&lt;SemanticSearchResult&gt;**](SemanticSearchResult.md) | 의미 기반 표준 용어 후보 | [default to undefined]

## Example

```typescript
import { SemanticSearchResponse } from '@aulms/api-client';

const instance: SemanticSearchResponse = {
    query,
    items,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
