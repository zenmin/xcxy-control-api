package com.zh.controlcenter.common.util;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
public class JWTUtil {

    private static final String TOKEN_SECRET = "com.zh";
    public static final Integer DAYS = 30; // 1个月
    public static final String DEVICE_TYPE = "t";    // 设备类型
    public static final String USERID = "u";    // 用户id

    /**
     * @return
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     */
    public static String create(Integer type, String uid) {
        try {
            return JWT.create()
                    .withClaim(DEVICE_TYPE, type)
                    .withClaim(USERID, uid)
                    .withExpiresAt(DateUtil.offsetDay(new Date(), DAYS))
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (IllegalArgumentException | JWTCreationException e) {
            log.error("JWT生成失败", e);
            return CommonUtil.UUID();
        }
    }

    /**
     * @param token
     * @return
     * @throws JWTVerificationException
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     */
    public static boolean verify(String token) {
        try {
            JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return
     */
    public static Integer getDeviceType(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim(DEVICE_TYPE).asInt();
        } catch (Exception e) {
            throw e;
        }
    }

}
