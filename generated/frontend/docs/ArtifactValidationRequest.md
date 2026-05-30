# ArtifactValidationRequest

개발 산출물 표준 용어 검증 요청

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**sourceType** | [**ArtifactSourceType**](ArtifactSourceType.md) |  | [default to undefined]
**filePath** | **string** | 검증 대상 파일 경로 | [default to undefined]
**content** | **string** | 검증 대상 파일 본문 | [default to undefined]
**domainNames** | **Array&lt;string&gt;** | 검증 대상 도메인 필터 | [optional] [default to undefined]
**failOnWarning** | **boolean** | WARNING도 실패 exitCode로 볼지 여부 | [optional] [default to false]
**includeSuggestions** | **boolean** | 권장 표현을 검증 결과에 포함할지 여부 | [optional] [default to true]

## Example

```typescript
import { ArtifactValidationRequest } from '@aulms/api-client';

const instance: ArtifactValidationRequest = {
    sourceType,
    filePath,
    content,
    domainNames,
    failOnWarning,
    includeSuggestions,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
