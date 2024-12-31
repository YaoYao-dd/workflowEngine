package com.yaod.workflow.engine.infra.springboot;

import com.yaod.workflow.engine.core.exception.ItemNotFoundExecption;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Yaod
 **/
@ControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { ItemNotFoundExecption.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected String handleNotFound(
            ItemNotFoundExecption ex, WebRequest request) {
        String bodyOfResponse = String.format("itemid %s not found", ex.getItemId());
        return bodyOfResponse;
    }
}