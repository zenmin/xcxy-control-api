package com.zh.controlcenter.common.apibase;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

/**
 * @Describle This Class Is 全局异常处理器
 * @Author ZengMin
 * @Date 2019/1/3 19:43
 */
@RestControllerAdvice
@Slf4j
public class CommonHandlerException extends RuntimeException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handler(RuntimeException e) {
        if (e instanceof HttpMessageNotReadableException) {
            return ResponseEntity.error(ApiResultCode.JSON_ERROR.getCode(), "Json参数类型异常！");
        }

        // 统一接口异常
        if (e instanceof CommonException) {
            return ResponseEntity.error(((CommonException) e).getCode(), e.getMessage(), ((CommonException) e).getData());
        }

        // SpringSecurity异常(登录/认证)
        if (e instanceof AuthenticationException) {
            if (e instanceof BadCredentialsException) {
                return ResponseEntity.error(ApiResultCode.LOGIN_ERROR.getCode(), "用户名或密码错误！");
            }
            return ResponseEntity.error(ApiResultCode.LOGIN_ERROR.getCode(), e.getMessage());
        }

        if (e instanceof AccessDeniedException) {
            return ResponseEntity.error(ApiResultCode.NOTAUTH_OPTION.getCode(), StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "您没有权限操作！");
        }
        // 文件上传异常
        if (e instanceof MultipartException) {
            return ResponseEntity.error(ApiResultCode.FILE_UPLOAD_ERROR.getCode(), "文件过大，请重新上传！");
        }
        e.printStackTrace();
        return ResponseEntity.error("系统异常，请稍后再试！");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.error(ApiResultCode.PARAMSERROR.getCode(), message);
    }

}
