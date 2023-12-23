package com.zh.controlcenter.common.config.security;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.zh.controlcenter.common.apibase.UserContextHolder;
import com.zh.controlcenter.common.entity.base.UserSimpleDo;
import com.zh.controlcenter.common.util.IpHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @Describle This Class Is Security EntryPoint  默认加入到Spring security的拦截器中
 * @Author ZengMin
 * @Date 2020/4/4 10:55
 */
@Component
@Slf4j
public class AuthenticationInterceptor implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpHeaders respHeader = exchange.getResponse().getHeaders();
        respHeader.addIfAbsent("Access-Control-Allow-Origin", "*");
        respHeader.addIfAbsent("Access-Control-Allow-Credentials", "true");
        respHeader.addIfAbsent("Access-Control-Max-Age", "36000");
        if (exchange.getRequest().getPath().value().startsWith("/api") && Objects.isNull(UserContextHolder.getLoginUser())) {
            String requestIpAddr = IpHelper.getIpAddress(exchange.getRequest());
            UserContextHolder.set(new UserSimpleDo(IdWorker.getId(), requestIpAddr));
//            log.info("reqip = " + requestIpAddr);
//        String token = headers.getFirst(CommonConstant.TOKEN);
            // 验证登陆
        }
        return chain.filter(exchange);
    }

}
