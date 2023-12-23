package com.zh.controlcenter.component;

import com.zh.controlcenter.common.apibase.CommonException;
import com.zh.controlcenter.common.apibase.UserContextHolder;
import com.zh.controlcenter.common.constant.CommonConstant;
import com.zh.controlcenter.common.constant.RateLimiter;
import com.zh.controlcenter.common.util.CacheConstant;
import com.zh.controlcenter.common.util.GuavaCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Describle This Class Is 限流Aspect
 * @Author ZengMin
 * @Date 2019/6/29 16:40
 */
@Component
@Aspect
@Slf4j
public class RateLimiterAspect {

    @Autowired
    GuavaCacheUtil guavaCache;

    /**
     * 全局 ：每秒最多处理10个请求
     */
    public static com.google.common.util.concurrent.RateLimiter guavaLimiter = com.google.common.util.concurrent.RateLimiter.create(10.0);

    /**
     * 方法+注释 切入点
     * 优先级 高
     */
    @Pointcut("@annotation(com.zh.controlcenter.common.constant.RateLimiter)")
    @Order(1)
    private void pointMethod() {
    }

    /**
     * 类+注释切入点
     * 优先级 低
     */
    @Pointcut("@within(com.zh.controlcenter.common.constant.RateLimiter)")
    @Order(2)
    private void pointCutClass() {
    }

    /**
     * 两个 满足一个
     *
     * @param joinPoint
     */
    @Before("pointMethod() || pointCutClass()")
    public void execAspect(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        this.validRate(method);
    }

    private void validRate(Method method) {
        RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
        if (Objects.isNull(rateLimiter)) {
            rateLimiter = method.getDeclaringClass().getAnnotation(RateLimiter.class);
        }
        try {
            this.limit(rateLimiter, method);
        } catch (Exception e) {
            if (e instanceof CommonException) {
                throw e;
            }
            e.printStackTrace();
        }
    }

    private boolean limit(RateLimiter rateLimiter, Method method) {
        // 限流数量 用户：每10秒的请求次数  全局：每秒接口响应次数
        String value = rateLimiter.value();
        // 限流目标
        String target = rateLimiter.target();
        // 用户操作限流
        if (CommonConstant.LIMIT_USER.equals(target)) {
            String id = UserContextHolder.getLoginUser().getId().toString();
            int limit = Integer.parseInt(value);
            String key = CacheConstant.USER_LIMIT + method.getName() + ":" + id;
            // 判断此用户在系统的操作次数
            Object o = guavaCache.get(key);
            if (Objects.nonNull(o)) {
                int i = Integer.parseInt(o.toString());
                if (i >= limit) {
                    throw new CommonException("操作频繁，请稍后再试！");
                } else {
                    guavaCache.put(key, i + 1);
                }
            } else {
                guavaCache.put(key, 1);
            }
        }

        // 接口全局限流
        if (CommonConstant.LIMIT_ALL.equals(target)) {
            boolean b = guavaLimiter.tryAcquire();
            if (b) {
                return true;
            } else {
                throw new CommonException("系统繁忙，请稍后再试！");
            }
        }

        // IP全局限流
        if (CommonConstant.LIMIT_USER_IP.equals(target)) {
            String ip = UserContextHolder.getLoginUser().getIp();
            if (StringUtils.isBlank(ip)) {
                return true;
            }
            int limit = Integer.parseInt(value);
            String key = CacheConstant.USER_LIMIT + method.getName() + ":" + ip;
            // 判断此IP在系统的操作次数
            Object o = guavaCache.get(key);
            if (Objects.nonNull(o)) {
                int i = Integer.parseInt(o.toString());
                if (i >= limit) {
                    throw new CommonException("操作频繁，请稍后再试！");
                } else {
                    guavaCache.put(key, i + 1);
                }
            } else {
                guavaCache.put(key, 1);
            }
        }
        return true;
    }
}

