# DocumentReviewResult

기획서 본문 용어 검토 결과

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**originalText** | **string** | 원본 기획서 본문 | [default to undefined]
**recommendedText** | **string** | 표준 용어로 치환한 추천 본문 | [default to undefined]
**detectedTerms** | [**Array&lt;DetectedTerm&gt;**](DetectedTerm.md) | 검출된 용어 표현 목록 | [default to undefined]
**standardMappings** | [**Array&lt;StandardMapping&gt;**](StandardMapping.md) | 표준 용어 매핑표 | [default to undefined]
**candidateTerms** | [**Array&lt;CandidateTerm&gt;**](CandidateTerm.md) | 데이터 사전에 매핑되지 않은 신규 용어 후보 | [default to undefined]
**validationIssues** | [**Array&lt;ValidationIssue&gt;**](ValidationIssue.md) | 표준 위반 또는 권고 사항 | [default to undefined]

## Example

```typescript
import { DocumentReviewResult } from '@aulms/api-client';

const instance: DocumentReviewResult = {
    originalText,
    recommendedText,
    detectedTerms,
    standardMappings,
    candidateTerms,
    validationIssues,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
