package com.zh.controlcenter.common.apibase;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Describle This Class Is 全局接口返回类
 * @Author ZengMin
 * @Date 2019/1/3 19:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ResponseEntity<T> implements Serializable {

    @Schema(description = "响应码 常见响应码（100：成功，101：资源未找到，102：参数异常，104：资源已存在，105：数据异常，700：没有权限操作，899：第三方平台通信异常，989：操作频繁，991：JSON格式异常，997：登录超时，999：系统异常，896：微信登陆统一异常）")
    private int code;

    @Schema(description = "响应说明")
    private String msg;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "响应数据")
    private boolean isSuccess;

    public static ResponseEntity success() {
        return new ResponseEntity(ApiResultCode.SUCCESS.getCode(), ApiResultCode.SUCCESS.getName(), null, true);
    }

    public static ResponseEntity success(Object data) {
        return new ResponseEntity(ApiResultCode.SUCCESS.getCode(), ApiResultCode.SUCCESS.getName(), data, true);
    }

    public static ResponseEntity error(int code, String msg) {
        return new ResponseEntity(code, msg, null, false);
    }

    public static ResponseEntity error(int code, String msg, Object data) {
        return new ResponseEntity(code, msg, data, false);
    }

    public static ResponseEntity error() {
        return new ResponseEntity(ApiResultCode.ERROR.getCode(), "失败", null, false);
    }

    public static ResponseEntity error(String msg) {
        return new ResponseEntity(ApiResultCode.ERROR.getCode(), msg, null, false);
    }
}
