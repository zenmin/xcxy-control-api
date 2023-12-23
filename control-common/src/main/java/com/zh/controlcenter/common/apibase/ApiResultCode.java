package com.zh.controlcenter.common.apibase;

import lombok.Getter;
import lombok.Setter;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2019/1/3 19:49
 */
public enum ApiResultCode {

    SUCCESS(100, "操作成功"),

    NOTFOUND(101, "资源未找到"),

    PARAMSERROR(102, "参数异常"),

    JSON_ERROR(102, "JSON格式异常"), // JSON格式异常

    TIME_ERROR(107, "时间格式异常"),       // 时间格式异常

    ISEXISTS(104, "资源已存在"),

    DATAERROR(105, "数据异常"),    // 数据异常

    JSONRROR(106, "JSON格式异常"),

    SMS_ERROR(221, "验证码发送失败"),  //验证码发送失败

    NOTAUTH_OPTION(700, "没有权限操作"),

    OPTION_ERROR(701, "应用级防火墙触发"),

    USER_NOT_REG(893, "用户未注册"),    // 用户未注册

    USER_NOT_PHONE(894, "用户未绑定手机号"),   // 用户未绑定手机号

    WX_SERVER_ERROR(896, "微信登录异常"),   // 微信统一异常

    LOGIN_ERROR(897, "登录异常"),    // 登录异常

    FILE_UPLOAD_ERROR(898, "文件上传异常"),    // 文件上传异常

    INTERFACE_ERROR(899, "接口调用失败"),  // 接口调用失败

    USER_LIMIT_ERROR(989, "操作频繁"), // 操作频繁

    USER_STATUS_ERROR(990, "用户状态异常"), // 用户状态异常

    USER_TIMEOUT_ERROR(871, "账号已经过期"), // 账号已经过期

    NOTAUTH(997, "登录超时，请重新登录"),     // 登录超时

    ERROR(999, "操作失败");    // 失败


    @Setter
    @Getter
    int code;

    @Setter
    @Getter
    String name;

    ApiResultCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
