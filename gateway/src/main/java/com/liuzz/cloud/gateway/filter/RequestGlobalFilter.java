package com.liuzz.cloud.gateway.filter;

import com.liuzz.cloud.common.core.constants.SecurityConstant;
import com.liuzz.cloud.common.core.constants.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;


/**
 * 全局请求过滤器
 *
 * @author liuzz
 */
@Slf4j
public class RequestGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 打印日志
        log.debug("方式:{} 主机:{} 地址:{} 参数:{}",
                exchange.getRequest().getMethod().name(),
                exchange.getRequest().getURI().getHost(),
                exchange.getRequest().getURI().getPath(),
                exchange.getRequest().getQueryParams());
        long startTime = System.currentTimeMillis();
        // 设置请求时间
        exchange.getAttributes().put(SystemConstant.REQUEST_START_TIME, System.currentTimeMillis());
        ServerHttpRequest request = exchange.getRequest().mutate().headers(headers -> {
            // 网关层移除恶意from请求头
            headers.remove(SecurityConstant.FROM);
            // 设置请求时间
            headers.put(SystemConstant.REQUEST_START_TIME, Collections.singletonList(String.valueOf(startTime)));
        }).build();
        // 重写StripPrefix
        addOriginalRequestUrl(exchange, request.getURI());
        String rawPath = request.getURI().getRawPath();
        String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/")).skip(1L)
                .collect(Collectors.joining("/"));
        ServerHttpRequest newRequest = request.mutate().path(newPath).build();
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());
        return chain.filter(exchange.mutate().request(newRequest).build()).then(Mono.fromRunnable(() -> {
            long executeTime = System.currentTimeMillis() - startTime;
            int code = 500;
            if (exchange.getResponse().getStatusCode() != null) {
                code = exchange.getResponse().getStatusCode().value();
            }
            // 打印请求日志
            log.debug("接口：{}，响应状态码：{}，请求耗时：{}ms", exchange.getRequest().getURI().getRawPath(), code, executeTime);
        }));
    }

    @Override
    public int getOrder() {
        //return 10;
        return Ordered.LOWEST_PRECEDENCE;
    }
}
