# RelationshipSearchResponse

Graphify 관계 검색 응답

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**query** | **string** | 검색 기준 | [default to undefined]
**items** | [**Array&lt;RelationshipSearchResult&gt;**](RelationshipSearchResult.md) | 관계 검색 결과 | [default to undefined]
**paths** | [**Array&lt;RelationshipPath&gt;**](RelationshipPath.md) | Graphify 그래프 경로 | [default to undefined]

## Example

```typescript
import { RelationshipSearchResponse } from '@aulms/api-client';

const instance: RelationshipSearchResponse = {
    query,
    items,
    paths,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
