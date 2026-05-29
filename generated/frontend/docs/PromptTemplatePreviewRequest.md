# PromptTemplatePreviewRequest

프롬프트 템플릿 미리보기 요청

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**requirementText** | **string** | 사용자가 입력한 요구사항 또는 기획서 초안 | [default to undefined]
**termMappings** | [**Array&lt;PromptTermMapping&gt;**](PromptTermMapping.md) | 데이터 사전 검색으로 확정한 표준 용어 매핑 목록 | [optional] [default to undefined]
**candidateTerms** | [**Array&lt;PromptCandidateTerm&gt;**](PromptCandidateTerm.md) | 데이터 사전에 없어 후보로 분리한 신규 용어 목록 | [optional] [default to undefined]
**additionalContext** | **string** | AI 도구에 함께 전달할 추가 맥락 | [optional] [default to undefined]

## Example

```typescript
import { PromptTemplatePreviewRequest } from '@aulms/api-client';

const instance: PromptTemplatePreviewRequest = {
    requirementText,
    termMappings,
    candidateTerms,
    additionalContext,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
