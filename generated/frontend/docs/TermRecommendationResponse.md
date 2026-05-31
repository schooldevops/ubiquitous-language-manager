# TermRecommendationResponse

RAG 조회, Graph 관계 확장, LLM 추론을 거쳐 생성한 용어 초안 추천 결과

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**inputKoreanName** | **string** |  | [default to undefined]
**normalizedKoreanName** | **string** |  | [default to undefined]
**recommendation** | [**RecommendedTermDraft**](RecommendedTermDraft.md) |  | [default to undefined]
**ragMatches** | [**Array&lt;RecommendationEvidence&gt;**](RecommendationEvidence.md) | RAG/semantic 검색으로 찾은 근거 용어 | [default to undefined]
**graphContext** | [**GraphRecommendationContext**](GraphRecommendationContext.md) |  | [default to undefined]
**llmReasoning** | **string** | 검색과 관계 문맥을 바탕으로 추천값을 조합한 추론 설명 | [default to undefined]
**warnings** | **Array&lt;string&gt;** |  | [default to undefined]

## Example

```typescript
import { TermRecommendationResponse } from '@aulms/api-client';

const instance: TermRecommendationResponse = {
    inputKoreanName,
    normalizedKoreanName,
    recommendation,
    ragMatches,
    graphContext,
    llmReasoning,
    warnings,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
