package com.exam.messagegateway.config;

import com.exam.messagegateway.dto.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    @ResponseBody
    public ResponseInfo handleValidException(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException me = (MethodArgumentNotValidException) e;
            return wrapperBindingResult(me.getBindingResult());
        }
        if (e instanceof BindException) {
            BindException be = (BindException) e;
            return wrapperBindingResult(be.getBindingResult());
        }
        if (e instanceof ConstraintViolationException) {
            return wrapperConstraintViolationExceptionResult((ConstraintViolationException) e);
        }
        return new ResponseInfo(400, "请求参数错误");
    }

    private ResponseInfo wrapperConstraintViolationExceptionResult(ConstraintViolationException e) {
        StringBuilder msg = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return new ResponseInfo(400, "请求参数错误");
        }
        for (ConstraintViolation<?> item : constraintViolations) {
            String propertyPath = item.getPropertyPath().toString();
            msg.append(", ").append(propertyPath.split("\\.")[1]).append(": ").append(item.getMessage());
        }

        return new ResponseInfo(400, "请求参数错误");
    }

    private ResponseInfo wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        if (CollectionUtils.isEmpty(allErrors)) {
            return new ResponseInfo(400, "请求参数错误");
        }
        for (ObjectError error : allErrors) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
        }

        return new ResponseInfo(400, "请求参数错误");
    }
}
