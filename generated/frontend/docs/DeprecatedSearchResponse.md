# DeprecatedSearchResponse

폐기어 검색 결과 목록 응답

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**query** | **string** | 사용자가 입력한 폐기어 또는 비표준 표현 | [default to undefined]
**items** | [**Array&lt;DeprecatedSearchResult&gt;**](DeprecatedSearchResult.md) | 폐기어와 대체 표준 용어 목록 | [default to undefined]

## Example

```typescript
import { DeprecatedSearchResponse } from '@aulms/api-client';

const instance: DeprecatedSearchResponse = {
    query,
    items,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
