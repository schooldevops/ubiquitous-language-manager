# DevelopmentAssistRequest

AI 산출물 생성 지원 요청

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**requirementText** | **string** | 사용자가 입력한 요구사항 | [default to undefined]
**targetArtifacts** | [**Array&lt;AssistTargetArtifact&gt;**](AssistTargetArtifact.md) | 생성할 산출물 유형 목록 | [default to undefined]
**domainNames** | **Array&lt;string&gt;** | 우선 검색할 업무 도메인 목록 | [optional] [default to undefined]

## Example

```typescript
import { DevelopmentAssistRequest } from '@aulms/api-client';

const instance: DevelopmentAssistRequest = {
    requirementText,
    targetArtifacts,
    domainNames,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
