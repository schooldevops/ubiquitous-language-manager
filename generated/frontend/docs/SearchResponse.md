# SearchResponse

검색 결과 목록 응답

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**query** | **string** | 사용자가 입력한 검색어 | [default to undefined]
**items** | [**Array&lt;SearchResult&gt;**](SearchResult.md) | 검색 결과 목록 | [default to undefined]
**page** | [**PageMetadata**](PageMetadata.md) |  | [optional] [default to undefined]

## Example

```typescript
import { SearchResponse } from '@aulms/api-client';

const instance: SearchResponse = {
    query,
    items,
    page,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
