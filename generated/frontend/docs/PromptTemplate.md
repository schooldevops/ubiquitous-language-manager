# PromptTemplate

프롬프트 템플릿 상세 정보

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**templateId** | **string** |  | [default to undefined]
**type** | [**PromptTemplateType**](PromptTemplateType.md) |  | [default to undefined]
**name** | **string** |  | [default to undefined]
**version** | **string** | SemVer 형식의 템플릿 버전 | [default to undefined]
**status** | [**PromptTemplateStatus**](PromptTemplateStatus.md) |  | [default to undefined]
**description** | **string** |  | [default to undefined]
**body** | **string** | Mustache 스타일 변수 플레이스홀더를 포함한 템플릿 본문 | [default to undefined]
**variables** | [**Array&lt;PromptTemplateVariable&gt;**](PromptTemplateVariable.md) | 템플릿에 주입 가능한 변수 목록 | [default to undefined]
**versionPolicy** | **string** | 템플릿 버전 변경 정책 | [default to undefined]
**histories** | [**Array&lt;PromptTemplateHistory&gt;**](PromptTemplateHistory.md) | 템플릿 변경 이력 | [default to undefined]
**createdAt** | **string** |  | [default to undefined]
**updatedAt** | **string** |  | [default to undefined]

## Example

```typescript
import { PromptTemplate } from '@aulms/api-client';

const instance: PromptTemplate = {
    templateId,
    type,
    name,
    version,
    status,
    description,
    body,
    variables,
    versionPolicy,
    histories,
    createdAt,
    updatedAt,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
