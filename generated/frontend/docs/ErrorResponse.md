# ErrorResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**code** | **string** | 시스템에서 정의한 에러 코드 | [default to undefined]
**message** | **string** | 사용자 또는 개발자가 이해할 수 있는 에러 메시지 | [default to undefined]
**detail** | **string** | 상세 에러 설명 | [optional] [default to undefined]
**traceId** | **string** | 요청 추적 ID | [default to undefined]

## Example

```typescript
import { ErrorResponse } from '@aulms/api-client';

const instance: ErrorResponse = {
    code,
    message,
    detail,
    traceId,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
