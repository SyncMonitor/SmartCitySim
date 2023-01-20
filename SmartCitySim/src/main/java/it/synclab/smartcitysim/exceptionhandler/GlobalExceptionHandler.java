package it.synclab.smartcitysim.exceptionhandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.synclab.smartcitysim.exception.NoHandlerFoundException;
import it.synclab.smartcitysim.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler 
    extends ResponseEntityExceptionHandler{

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ){
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return buildErrorResponse(exception, processFieldErrors(fieldErrors), HttpStatus.BAD_REQUEST, request);
    }

    private String processFieldErrors(List<FieldError> fieldErrors) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError: fieldErrors) {
            errors.add(fieldError.getField() + " " + fieldError.getDefaultMessage());
        }

        return errors.toString();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception, WebRequest request){
        return buildErrorResponse(exception, "Error violation of an integrity constraint", HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, WebRequest request){
        return buildErrorResponse(exception, "No endpoint found.", HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception, WebRequest request){
        return buildErrorResponse(exception, exception.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request){
        return buildErrorResponse(exception, exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
        Exception exception, String message, HttpStatusCode httpStatus, WebRequest request
    ){
        ErrorResponse errorResponse = new ErrorResponse(
            httpStatus.value(), 
            message, 
            getCurrentTimestamp(),
            exception.getClass().getCanonicalName(),
            ((ServletWebRequest)request).getRequest().getRequestURI().toString(),
            ((ServletWebRequest)request).getHttpMethod().toString()
        );
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(
        Exception ex,
        @Nullable
        Object body,
        HttpHeaders headers,
        HttpStatusCode statusCode,
        WebRequest request){
        return buildErrorResponse(ex, ex.getMessage(), statusCode, request);
    }

    //TODO: move this method in a proper class
    private Instant getCurrentTimestamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toInstant();
    }
}
