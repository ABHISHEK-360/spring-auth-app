package dev.a360.springAuth;

import dev.a360.springAuth.exceptions.ApiErrorModel;
import dev.a360.springAuth.exceptions.InvalidCredentialException;
import dev.a360.springAuth.exceptions.MissingParamsException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiErrorModel(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorModel apiError = new ApiErrorModel(BAD_REQUEST);
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        apiError.setMessage("Invalid/Missing Params");
        apiError.setDebugMessage(errors.toString());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MissingParamsException.class)
    protected ResponseEntity<Object> handleRequestParamsMissing(MissingParamsException ex) {
        ApiErrorModel apiError = new ApiErrorModel(BAD_REQUEST);

        apiError.setMessage("Missing Params");
        apiError.setDebugMessage(ex.getMessage());

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityException(DataIntegrityViolationException ex) {
        ApiErrorModel apiError = new ApiErrorModel(BAD_REQUEST);
        String debug = Objects.requireNonNull(ex.getRootCause()).toString().split("Detail: ")[1];

        apiError.setMessage("Invalid Params");
        apiError.setDebugMessage(debug);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    protected ResponseEntity<Object> handleDataIntegrityException(InvalidCredentialException ex) {
        ApiErrorModel apiError = new ApiErrorModel(BAD_REQUEST);

        apiError.setMessage("Invalid Username/Password");

        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorModel apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
