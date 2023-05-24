package com.eewoo.platform.handler;

import com.eewoo.common.util.R;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理，以免服务器报错输出，里面包含常见错误
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 一般参数（form-data）校验绑定异常处理
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public R bindException(BindException ex) {
        StringBuilder msg = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(e -> { // 获取所有的错误信息
            msg.append(e.getDefaultMessage()).append(" ");
            System.out.println(e.getDefaultMessage());
        });
        System.out.println("bindException");
        return R.err(String.valueOf(msg));
    }

    /**
     * JSON参数校验绑定异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public R methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder msg = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(e -> {
            msg.append(e.getDefaultMessage()).append(" ");
            System.out.println(e.getDefaultMessage());
        });
        return R.err(String.valueOf(msg));
    }

    /**
     * 单个参数异常处理
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public R constraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();  // 获取具体的错误信息
        StringBuilder str = new StringBuilder();
        violations.forEach(e -> {
            str.append(e.getMessage()).append(" ");
            System.out.println(e.getMessage());
        });    // 打印数据
        return R.err(String.valueOf(str));
    }


    /**
     * 其他异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R otherException(Exception ex) {
        ex.printStackTrace();
        return R.err("服务器内部异常错误!\n"+ex.getMessage());
    }
}