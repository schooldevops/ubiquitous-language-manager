# ValidationIssue


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**severity** | [**ValidationSeverity**](ValidationSeverity.md) |  | [default to undefined]
**source** | **string** | 문서, DDL, OpenAPI, 코드, PR 등 검증 출처 | [default to undefined]
**location** | **string** | 파일명, 라인, 필드명 등 위치 정보 | [optional] [default to undefined]
**inputExpression** | **string** | 입력 표현 | [default to undefined]
**standardTerm** | **string** | 표준 용어 | [optional] [default to undefined]
**recommendedExpression** | **string** | 권장 표현 | [optional] [default to undefined]
**reason** | **string** | 위반 또는 권고 사유 | [default to undefined]

## Example

```typescript
import { ValidationIssue } from '@aulms/api-client';

const instance: ValidationIssue = {
    severity,
    source,
    location,
    inputExpression,
    standardTerm,
    recommendedExpression,
    reason,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
