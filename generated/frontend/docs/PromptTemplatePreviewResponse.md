# PromptTemplatePreviewResponse

프롬프트 템플릿 미리보기 응답

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**templateId** | **string** |  | [default to undefined]
**version** | **string** |  | [default to undefined]
**renderedPrompt** | **string** | 변수 주입이 완료된 최종 프롬프트 | [default to undefined]
**injectedVariables** | **Array&lt;string&gt;** | 실제 주입된 변수명 목록 | [default to undefined]

## Example

```typescript
import { PromptTemplatePreviewResponse } from '@aulms/api-client';

const instance: PromptTemplatePreviewResponse = {
    templateId,
    version,
    renderedPrompt,
    injectedVariables,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
