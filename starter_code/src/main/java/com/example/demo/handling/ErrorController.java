package com.example.demo.handling;

import com.example.demo.controllers.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/* thanks to Author of this link https://www.jvt.me/posts/2020/10/29/spring-log-all-exceptions/*/
@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    private static final Logger logger= LoggerFactory.getLogger(ErrorController.class);
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAll(Exception ex, WebRequest request ) {
        ApiError apiErr = new ApiError(new Date(), ex.getMessage(), request.getDescription(false));
          logger.trace("Exceptions: exception happened with following details : ", ex.getCause());
          return new ResponseEntity(apiErr, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex,
                                                             @Nullable Object body,
                                                             @NonNull HttpHeaders headers,
                                                             HttpStatus status,
                                                             @NonNull WebRequest request){
        if (status.is5xxServerError()) {
            logger.error("An exception occured, which will cause a {} response", status, ex);
        } else if (status.is4xxClientError()){
            logger.warn("An exception occured, which will cause a {} response", status, ex);
        } else {
            logger.debug("An exception occured, which will cause a {} response", status, ex);
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

}
