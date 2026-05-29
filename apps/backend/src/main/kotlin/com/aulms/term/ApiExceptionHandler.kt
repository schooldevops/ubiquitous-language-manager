package com.aulms.term

import com.aulms.candidate.CandidateNotFoundException
import com.aulms.model.ErrorResponse
import com.aulms.prompt.PromptTemplateNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.UUID

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(TermNotFoundException::class)
    fun handleNotFound(exception: TermNotFoundException): ResponseEntity<ErrorResponse> =
        error(HttpStatus.NOT_FOUND, "TERM_NOT_FOUND", exception.message ?: "표준 용어를 찾을 수 없습니다.")

    @ExceptionHandler(CandidateNotFoundException::class)
    fun handleCandidateNotFound(exception: CandidateNotFoundException): ResponseEntity<ErrorResponse> =
        error(HttpStatus.NOT_FOUND, "CANDIDATE_NOT_FOUND", exception.message ?: "신규 용어 후보를 찾을 수 없습니다.")

    @ExceptionHandler(PromptTemplateNotFoundException::class)
    fun handlePromptTemplateNotFound(exception: PromptTemplateNotFoundException): ResponseEntity<ErrorResponse> =
        error(HttpStatus.NOT_FOUND, "PROMPT_TEMPLATE_NOT_FOUND", exception.message ?: "프롬프트 템플릿을 찾을 수 없습니다.")

    @ExceptionHandler(TermConflictException::class)
    fun handleConflict(exception: TermConflictException): ResponseEntity<ErrorResponse> =
        error(HttpStatus.CONFLICT, "TERM_CONFLICT", exception.message ?: "표준 용어 상태가 충돌합니다.")

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(exception: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> =
        error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", exception.bindingResult.allErrors.firstOrNull()?.defaultMessage ?: "요청 값이 유효하지 않습니다.")

    private fun error(status: HttpStatus, code: String, message: String): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(status).body(
            ErrorResponse(
                code = code,
                message = message,
                traceId = UUID.randomUUID().toString(),
            ),
        )
}
