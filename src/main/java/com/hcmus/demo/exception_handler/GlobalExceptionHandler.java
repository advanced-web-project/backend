package com.hcmus.demo.exception_handler;

import com.hcmus.demo.exception_handler.exception.UserNotFoundException;
import com.hcmus.demo.security.jwt.JwtValidationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;

/**
 * Global exception handler for the application.
 * This class handles various exceptions and provides appropriate responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles general exceptions.
     *
     * @param request the HTTP request
     * @param ex      the exception
     * @return an ErrorDTO containing error details
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGeneralException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();
        error.setTimestamp(new Date());
        error.setPath(request.getServletPath());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.addError(ex.getMessage());
        LOGGER.error(ex.getMessage(), ex);
        return error;
    }

    /**
     * Handles EntityNotFoundException.
     *
     * @param ex      the exception
     * @param request the web request
     * @return an ErrorDTO containing error details
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ErrorDTO error = new ErrorDTO();
        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setPath((((ServletWebRequest) request).getRequest().getServletPath()));
        error.addError(ex.getMessage());
        return error;
    }

    /**
     * Handles DataIntegrityViolationException.
     *
     * @param ex      the exception
     * @param request the web request
     * @return an ErrorDTO containing error details
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorDTO handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ErrorDTO error = new ErrorDTO();
        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setPath((((ServletWebRequest) request).getRequest().getServletPath()));
        error.addError(ex.getMessage());
        return error;
    }

    /**
     * Handles TransactionSystemException.
     *
     * @param ex      the exception
     * @param request the web request
     * @return an ErrorDTO containing error details
     */
    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleTransactionSystemException(TransactionSystemException ex, WebRequest request) {
        LOGGER.error("Transaction system exception", ex);
        ErrorDTO error = new ErrorDTO();
        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setPath((((ServletWebRequest) request).getRequest().getServletPath()));
        error.addError(ex.getMessage());
        return error;
    }

    /**
     * Handles MethodArgumentNotValidException.
     *
     * @param ex      the exception
     * @param headers the HTTP headers
     * @param status  the HTTP status code
     * @param request the web request
     * @return a ResponseEntity containing error details
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ErrorDTO error = new ErrorDTO();
        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setPath((((ServletWebRequest) request).getRequest().getServletPath()));

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            error.addError(fieldError.getDefaultMessage());
        });
        return new ResponseEntity<>(error, headers, status);
    }

    /**
     * Handles JwtValidationException.
     *
     * @param request the HTTP request
     * @param ex      the exception
     * @return an ErrorDTO containing error details
     */
    @ExceptionHandler(JwtValidationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO handleJwtValidationException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();
        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.addError(ex.getMessage());
        error.setPath(request.getServletPath());
        LOGGER.error(ex.getMessage(), ex);
        return error;
    }

    /**
     * Handles UsernameNotFoundException and UserNotFoundException.
     *
     * @param request the HTTP request
     * @param ex      the exception
     * @return an ErrorDTO containing error details
     */
    @ExceptionHandler({UsernameNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO UserNotFoundException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();
        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.addError("User not found");
        error.setPath(request.getServletPath());
        LOGGER.error(ex.getMessage(), ex);
        return error;
    }

    /**
     * Handles BadCredentialsException.
     *
     * @param request the HTTP request
     * @param ex      the exception
     * @return an ErrorDTO containing error details
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO BadCredentialException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();
        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.addError("Bad credential!");
        error.setPath(request.getServletPath());
        LOGGER.error(ex.getMessage());
        return error;
    }
}
