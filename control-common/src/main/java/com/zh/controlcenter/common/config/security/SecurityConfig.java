package com.zh.controlcenter.common.config.security;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Describle This Class Is SpringSecurity配置
 * @Author ZengMin
 * @Date 2019/4/13 11:45
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * 必须认证访问的URI
     */
    public static final String[] AUTH_MATCHERS = {"/**"};


    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;


    /**
     * 允许匿名访问URI  不需要带前缀  如：/api/order,/api/user
     */
    public static String[] NOAUTH_MATCHERS = {};

    static {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("whiteuri.properties"));
            String pros = properties.get("white-uri").toString();
            NOAUTH_MATCHERS = pros.split(StringPool.COMMA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .authorizeExchange()//请求进行授权
                .pathMatchers(AUTH_MATCHERS).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()//特殊请求过滤
                .pathMatchers(NOAUTH_MATCHERS).permitAll()//不需要验证
                .and()
                .exceptionHandling()
                .accessDeniedHandler((exchange, denied) -> { // 无权限访问处理器(可以单独创建类处理)
                    Map<String, String> responseMap = new HashMap<>(2);
                    responseMap.put("code", "denied");
                    responseMap.put("msg", "账户无权限访问");
                    return writeWith(exchange, responseMap);
                })
                .and().addFilterBefore(authenticationInterceptor, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    /**
     * 输出响应信息
     *
     * @param exchange
     * @param responseMap
     * @return
     */
    public Mono<Void> writeWith(ServerWebExchange exchange, Map<String, String> responseMap) {
        ServerHttpResponse response = exchange.getResponse();
        String body = JSONObject.toJSONString(responseMap);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(Charset.defaultCharset()));
        return response.writeWith(Mono.just(buffer));
    }

}