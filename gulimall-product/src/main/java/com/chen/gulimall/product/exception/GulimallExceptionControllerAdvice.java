package com.chen.gulimall.product.exception;

import com.chen.gulimall.base.exception.BizCodeEume;
import com.chen.gulimall.base.utils.R;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/20 22:17
 * @description 异常处理
 */

@RestControllerAdvice(basePackages = "com.chen.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e) {
        Map<String, String> map = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((item) -> {
            String defaultMessage = item.getDefaultMessage();
            String field = item.getField();
            map.put(field, defaultMessage);
        });
        return R.error(BizCodeEume.VAILD_EXCEPTION.getCode(), BizCodeEume.VAILD_EXCEPTION.getMessage()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable e) {
        return R.error(BizCodeEume.UNKNOW_EXCEPTION.getCode(), BizCodeEume.UNKNOW_EXCEPTION.getMessage());
    }
}
