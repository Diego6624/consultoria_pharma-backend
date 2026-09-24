package com.pharma.consultoria_pharma.exceptions;

import com.pharma.consultoria_pharma.dto.response.ApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", request.getRequestURI(), null);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiErrorResponse> handleDisabled(DisabledException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", request.getRequestURI(), null);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleRateLimit(RateLimitExceededException ex, HttpServletRequest request) {
        return buildError(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildError(HttpStatus.FORBIDDEN, "No tiene permisos para esta operación", request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "Error de validación", request.getRequestURI(),
                ex.getBindingResult().getFieldErrors().stream().map(this::toFieldError).toList());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiErrorResponse> handleBinding(BindException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "Error de validación", request.getRequestURI(),
                ex.getBindingResult().getFieldErrors().stream().map(this::toFieldError).toList());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<ApiErrorResponse.FieldErrorResponse> errors = ex.getConstraintViolations().stream()
                .map(violation -> ApiErrorResponse.FieldErrorResponse.builder()
                        .field(violation.getPropertyPath().toString())
                        .message(violation.getMessage())
                        .build())
                .toList();
        return buildError(HttpStatus.BAD_REQUEST, "Error de validación", request.getRequestURI(), errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ApiErrorResponse.FieldErrorResponse error = ApiErrorResponse.FieldErrorResponse.builder()
                .field(ex.getName())
                .message("El valor proporcionado no es válido")
                .build();
        return buildError(HttpStatus.BAD_REQUEST, "Parámetro inválido", request.getRequestURI(), List.of(error));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        ApiErrorResponse.FieldErrorResponse error = ApiErrorResponse.FieldErrorResponse.builder()
                .field(ex.getParameterName())
                .message("El parámetro es obligatorio")
                .build();
        return buildError(HttpStatus.BAD_REQUEST, "Parámetro faltante", request.getRequestURI(), List.of(error));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<ApiErrorResponse> handleMalformedRequest(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "El formato de la solicitud no es válido", request.getRequestURI(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "Uno de los parámetros enviados no es válido", request.getRequestURI(), null);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidSort(PropertyReferenceException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "El criterio de ordenamiento no es válido", request.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado en {}", request.getRequestURI(), ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request.getRequestURI(), null);
    }

    private ApiErrorResponse.FieldErrorResponse toFieldError(FieldError fieldError) {
        return ApiErrorResponse.FieldErrorResponse.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build();
    }

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String message,
            String path,
            List<ApiErrorResponse.FieldErrorResponse> fieldErrors) {

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(status).body(body);
    }
}
