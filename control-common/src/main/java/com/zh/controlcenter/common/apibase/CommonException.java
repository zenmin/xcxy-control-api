package com.zh.controlcenter.common.apibase;

import lombok.Data;

/**
 * 通用异常
 *
 * @author liuxiaolong
 * @version 1.0
 * @since 2021/7/2 上午 10:17
 */
@Data
public class CommonException extends RuntimeException {

    public int code;

    public Object data;

    public CommonException(String msg) {
        super(msg);
        this.code = 999;
    }

    public CommonException(int code, String message) {
        this(code, message, null);
    }

    public CommonException(int code, String message,
                           Throwable cause) {
        super(message, cause);
        this.code = code;
    }


}
