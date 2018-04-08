package com.sdsu.edu.cms.cmsservice.exceptions;

import com.sdsu.edu.cms.common.models.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ConferenceMgmtExceptionHandler extends ResponseEntityExceptionHandler {
    private ApiError apiError;
    @ExceptionHandler(ApiErrorException.class)
    public final ResponseEntity<Object> handleApiErrors(Exception ex, WebRequest request) {
        apiError = new ApiError("bad_request", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConferenceNotFoundException.class)
    public final ResponseEntity<Object> handleConferenceNotFoundErrors(Exception ex, WebRequest request) {
        apiError = new ApiError("not_found", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        apiError = new ApiError("internal_server_error", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
