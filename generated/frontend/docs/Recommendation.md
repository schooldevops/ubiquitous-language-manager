# Recommendation

표준 용어 사용 권고

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**action** | **string** | 권고 조치 | [default to undefined]
**recommendedTermId** | **string** | 권고 표준 용어 식별자 | [optional] [default to undefined]
**recommendedTerm** | **string** | 권고 표준 용어명 | [optional] [default to undefined]
**recommendedExpression** | **string** | 권고 표준 표현 | [default to undefined]
**reason** | **string** | 권고 사유 | [default to undefined]
**severity** | **string** | 권고 중요도 | [optional] [default to undefined]

## Example

```typescript
import { Recommendation } from '@aulms/api-client';

const instance: Recommendation = {
    action,
    recommendedTermId,
    recommendedTerm,
    recommendedExpression,
    reason,
    severity,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
