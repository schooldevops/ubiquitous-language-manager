# DocumentReviewOptions

기획서 용어 검토 옵션

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**includeCandidateTerms** | **boolean** | 미매핑 표현을 신규 용어 후보로 반환할지 여부 | [optional] [default to true]
**includeValidationIssues** | **boolean** | Rule Engine 검증 결과를 포함할지 여부 | [optional] [default to true]
**normalizeSentences** | **boolean** | 표준화 문장 추천을 포함할지 여부 | [optional] [default to true]

## Example

```typescript
import { DocumentReviewOptions } from '@aulms/api-client';

const instance: DocumentReviewOptions = {
    includeCandidateTerms,
    includeValidationIssues,
    normalizeSentences,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
