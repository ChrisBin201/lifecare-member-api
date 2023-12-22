package com.example.lifecaremember.handler;

import com.example.lifecaremember.dto.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData<String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(fe -> fe.getDefaultMessage()).collect(Collectors.toList());
        log.error("MethodArgumentNotValidException: ", ex);
        return new ResponseEntity<>(getErrorsMap(errors,HttpStatus.BAD_REQUEST), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private ResponseData<String> getErrorsMap(List<String> errors, HttpStatus httpStatus) {
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("error", errorCode);
//        errorResponse.put("message", errors.size()>1 ? errors: errors.get(0));
//        errorResponse.put("timestamp", LocalDateTime.now());
        ResponseData<String> errorResponse = new ResponseData<>();
        errorResponse.setSuccess(false);
        errorResponse.setCode(httpStatus.value());
        errorResponse.setStatus(httpStatus.name());
        String errorMessage = String.join(", ", errors);
        errorResponse.setMessage(errorMessage);
        errorResponse.setData(null);
        return errorResponse;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseData<String>> handleNotFoundException(UserNotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("UserNotFoundException: ", ex);
        return new ResponseEntity<>(getErrorsMap(errors,HttpStatus.NOT_FOUND), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseData<String>> handleAccessDeniedException(AccessDeniedException ex) {
        List<String> errors = Collections.singletonList("Access Denied, please contact to your administrator");
        log.error("AccessDeniedException: ", ex);
        return new ResponseEntity<>(getErrorsMap(errors,HttpStatus.FORBIDDEN), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ResponseData<String>> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList("Something went wrong");
        log.error("Exception: ", ex);
        return new ResponseEntity<>(getErrorsMap(errors,HttpStatus.INTERNAL_SERVER_ERROR), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ResponseData<String>> handleRuntimeExceptions(RuntimeException ex) {
        List<String> errors = Collections.singletonList("Something went wrong");
        log.error("RuntimeException: ", ex);
        return new ResponseEntity<>(getErrorsMap(errors,HttpStatus.BAD_REQUEST), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommonException.class)
    public final ResponseEntity<ResponseData<String>> handleCommonExceptions(CommonException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        int httpStatusCode = ex.getHttpStatusCode();
        HttpStatus httpStatus = HttpStatus.valueOf(httpStatusCode);
        String errorCode = ex.getErrorCode();
        log.error("CommonException: ", ex);
        return new ResponseEntity<>(getErrorsMap(errors, httpStatus), new HttpHeaders(), HttpStatus.valueOf(httpStatusCode));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
        public UserNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
