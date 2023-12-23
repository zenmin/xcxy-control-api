package com.zh.controlcenter.common.apibase;


import com.alibaba.ttl.TransmittableThreadLocal;
import com.zh.controlcenter.common.entity.base.UserSimpleDo;

/**
 * @Describle This Class Is 缓存租户信息
 * @Author ZengMin
 * @Date 2020/9/9 17:09
 */
public class UserContextHolder {

    /**
     * 支持父子线程之间的数据传递
     */
    private static final ThreadLocal<UserSimpleDo> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>(false);

    /**
     * <b>谨慎使用此方法,避免嵌套调用 </b>
     *
     * @param userSimpleDo 用户信息
     */
    public static void set(UserSimpleDo userSimpleDo) {
        THREAD_LOCAL_TENANT.set(userSimpleDo);
    }

    /**
     * 获取TTL中的用户
     *
     * @return
     */
    public static UserSimpleDo getLoginUser() {
        return THREAD_LOCAL_TENANT.get();
    }

    /**
     * 清除用户信息
     */
    public static void clear() {
        THREAD_LOCAL_TENANT.remove();
    }

}
