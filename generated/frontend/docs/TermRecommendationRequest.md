# TermRecommendationRequest

한글 용어명을 기준으로 RAG, Graph, 추론 단계를 거쳐 용어 등록 초안을 추천하는 요청

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**koreanName** | **string** | 추천 기준이 되는 한글 용어명 | [default to undefined]
**mode** | [**TermRecommendationMode**](TermRecommendationMode.md) |  | [default to undefined]
**currentDomainName** | **string** | 화면에서 이미 선택된 도메인 | [optional] [default to undefined]
**currentBusinessDefinition** | **string** | 사용자가 일부 입력한 업무 정의 | [optional] [default to undefined]
**currentUsageContext** | **string** | 사용자가 일부 입력한 사용 맥락 | [optional] [default to undefined]

## Example

```typescript
import { TermRecommendationRequest } from '@aulms/api-client';

const instance: TermRecommendationRequest = {
    koreanName,
    mode,
    currentDomainName,
    currentBusinessDefinition,
    currentUsageContext,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
