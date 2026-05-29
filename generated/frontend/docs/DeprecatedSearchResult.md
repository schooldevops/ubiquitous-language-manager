# DeprecatedSearchResult

폐기어 검색 결과와 대체 표준 용어

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**deprecatedExpression** | **string** | 폐기되었거나 사용 금지된 표현 | [default to undefined]
**reason** | **string** | 폐기 또는 사용 금지 사유 | [default to undefined]
**replacementTerm** | [**SearchResult**](SearchResult.md) |  | [default to undefined]
**recommendation** | [**Recommendation**](Recommendation.md) |  | [default to undefined]

## Example

```typescript
import { DeprecatedSearchResult } from '@aulms/api-client';

const instance: DeprecatedSearchResult = {
    deprecatedExpression,
    reason,
    replacementTerm,
    recommendation,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
