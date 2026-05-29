# DevelopmentAssistResponse

AI 산출물 생성 지원 응답

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**requirementText** | **string** |  | [default to undefined]
**extractedConcepts** | [**Array&lt;ExtractedBusinessConcept&gt;**](ExtractedBusinessConcept.md) | 요구사항에서 추출한 업무 개념 후보 | [default to undefined]
**termMappings** | [**Array&lt;AssistTermMapping&gt;**](AssistTermMapping.md) | 데이터 사전 기준 표준 용어 매핑 | [default to undefined]
**generatedArtifacts** | [**Array&lt;GeneratedArtifact&gt;**](GeneratedArtifact.md) | 표준 용어 매핑으로 생성한 산출물 예시 | [default to undefined]
**candidateTerms** | [**Array&lt;AssistCandidateTerm&gt;**](AssistCandidateTerm.md) | 데이터 사전에 없어 승인 전 후보로 분리한 신규 용어 | [default to undefined]
**warnings** | [**Array&lt;StandardViolationWarning&gt;**](StandardViolationWarning.md) | Rule Engine과 표준 정책 기반 위반 경고 | [default to undefined]

## Example

```typescript
import { DevelopmentAssistResponse } from '@aulms/api-client';

const instance: DevelopmentAssistResponse = {
    requirementText,
    extractedConcepts,
    termMappings,
    generatedArtifacts,
    candidateTerms,
    warnings,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
