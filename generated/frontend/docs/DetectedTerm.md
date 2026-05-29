# DetectedTerm

기획서 본문에서 검출된 용어 표현

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**expression** | **string** | 본문에서 검출된 표현 | [default to undefined]
**sentenceIndex** | **number** | 문장 번호 | [default to undefined]
**startOffset** | **number** | 본문 내 시작 위치 | [default to undefined]
**endOffset** | **number** | 본문 내 종료 위치 | [default to undefined]
**matchType** | **string** | 검출 방식 | [default to undefined]
**mappedTermId** | **string** | 매핑된 표준 용어 ID | [optional] [default to undefined]
**standardTerm** | **string** | 매핑된 표준 용어명 | [optional] [default to undefined]

## Example

```typescript
import { DetectedTerm } from '@aulms/api-client';

const instance: DetectedTerm = {
    expression,
    sentenceIndex,
    startOffset,
    endOffset,
    matchType,
    mappedTermId,
    standardTerm,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
