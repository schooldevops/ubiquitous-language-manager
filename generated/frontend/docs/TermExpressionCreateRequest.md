# TermExpressionCreateRequest

용어 표현 매핑 등록 요청

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**expressionType** | [**ExpressionType**](ExpressionType.md) |  | [default to undefined]
**expressionValue** | **string** |  | [default to undefined]
**language** | **string** |  | [optional] [default to undefined]
**style** | **string** |  | [optional] [default to undefined]
**isStandard** | **boolean** |  | [default to undefined]

## Example

```typescript
import { TermExpressionCreateRequest } from '@aulms/api-client';

const instance: TermExpressionCreateRequest = {
    expressionType,
    expressionValue,
    language,
    style,
    isStandard,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
