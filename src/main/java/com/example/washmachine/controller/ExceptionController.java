package com.example.washmachine.controller;

import com.example.washmachine.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Main exceptions controller.
 */
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    /**
     * ServiceException handler.
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler( { ServiceException.class })
    protected ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
        LOGGER.warn(ex.getLocalizedMessage());
        return new ResponseEntity<>(ex.getId() + ":" + ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
}
