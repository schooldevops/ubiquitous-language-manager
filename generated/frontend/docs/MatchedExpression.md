# MatchedExpression

검색어가 매칭된 용어 표현 정보

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**expressionType** | [**ExpressionType**](ExpressionType.md) |  | [default to undefined]
**expressionValue** | **string** | 데이터 사전에 등록된 표현값 | [default to undefined]
**matchType** | **string** | 검색어와 표현값의 매칭 방식 | [default to undefined]
**inputExpression** | **string** | 사용자가 입력한 표현 | [optional] [default to undefined]

## Example

```typescript
import { MatchedExpression } from '@aulms/api-client';

const instance: MatchedExpression = {
    expressionType,
    expressionValue,
    matchType,
    inputExpression,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
