package com.zh.controlcenter.common.constant;


/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2019/1/17 21:11
 */
public class CommonConstant {

    public static final String TOKEN = "token";  // 系统key

    public static final String PASSWORD_SECRET = "da656b93c48646a1";

    public static final int STATUS_OK = 1;  // ok

    public static final int STATUS_ERROR = 0;   // error

    public static final int STATUS_VALID = 2;   // 身份待认证

    public static final int MAGIC_ZERO = 0;  // 全局魔法值0

    public static final int MAGIC_ONE = 1;  // 全局魔法值 1

    public static final int MAGIC_TWO = 2;  // 全局魔法值 2

    public static final Integer MAGIC_THREE = 3;  // 全局魔法值 3

    public static final String MAGIC_SPLIT = ",";  // 全局魔法值 ，

    public static final String LIMIT_USER = "LIMIT_USER";       // 用户限流

    public static final String LIMIT_ALL = "LIMIT_ALL";       // 所有用户限流 接口每秒请求限制

    public static final String LIMIT_USER_IP = "LIMIT_USER_IP";       // IP限流

    public static final Integer DEVICE_TYPE_PC = 1;  // 设备类型 PC端


}
