# StandardMapping

기획서 표현과 표준 용어 산출물 표현 매핑

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**inputExpression** | **string** | 기획서 입력 표현 | [default to undefined]
**termId** | **string** | 표준 용어 ID | [default to undefined]
**standardTerm** | **string** | 한글 표준 용어명 | [default to undefined]
**englishName** | **string** | 영문 표준 용어명 | [default to undefined]
**dbColumn** | **string** | 표준 DB 컬럼명 | [default to undefined]
**apiField** | **string** | 표준 API 필드명 | [default to undefined]
**codeVariable** | **string** | 표준 코드 변수명 | [optional] [default to undefined]
**reason** | **string** | 표준 매핑 또는 권고 사유 | [default to undefined]

## Example

```typescript
import { StandardMapping } from '@aulms/api-client';

const instance: StandardMapping = {
    inputExpression,
    termId,
    standardTerm,
    englishName,
    dbColumn,
    apiField,
    codeVariable,
    reason,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
