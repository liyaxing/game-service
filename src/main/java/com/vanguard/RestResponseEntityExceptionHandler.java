package com.vanguard;

import com.vanguard.exception.FileIOException;
import com.vanguard.exception.FileValidationException;
import com.vanguard.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://example.com/resource-not-found"));
        return problemDetail;
    }

    @ExceptionHandler(value = FileIOException.class)
    protected ProblemDetail handleFileIOException(FileIOException ex, WebRequest request) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("File Processing Error");
        problemDetail.setType(URI.create("https://example.com/file-processing-error"));
        return problemDetail;
    }

    @ExceptionHandler(value = FileValidationException.class)
    protected ProblemDetail handleFileValidationException(FileValidationException ex, WebRequest request) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("File Validation Error");
        problemDetail.setType(URI.create("https://example.com/file-validation-error"));
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://example.com/internal-server-error"));
        return problemDetail;
    }
}
