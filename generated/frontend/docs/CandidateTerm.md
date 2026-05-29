# CandidateTerm

데이터 사전에 없는 신규 용어 후보

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**expression** | **string** | 신규 후보 표현 | [default to undefined]
**recommendedKoreanName** | **string** | 추천 한글 표준 용어명 | [optional] [default to undefined]
**reason** | **string** | 후보로 판단한 사유 | [default to undefined]
**approvalRequired** | **boolean** | 데이터 표준 승인 필요 여부 | [default to undefined]

## Example

```typescript
import { CandidateTerm } from '@aulms/api-client';

const instance: CandidateTerm = {
    expression,
    recommendedKoreanName,
    reason,
    approvalRequired,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
