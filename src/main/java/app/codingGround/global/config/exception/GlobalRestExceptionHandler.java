package app.codingGround.global.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    private static final String ERROR_LOG_MESSAGE = "[ERROR] {} : {}";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        log.warn(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, fieldErrors.get(fieldErrors.size()-1).getDefaultMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage(), e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.REQUIRED_REQUEST_NO_PARAMETER;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(CustomException e) {
        log.warn(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage(), e);
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

}
