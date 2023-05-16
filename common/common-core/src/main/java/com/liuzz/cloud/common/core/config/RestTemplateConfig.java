package com.liuzz.cloud.common.core.config;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Rest客户端配置
 *
 * @author liuzz
 */
@AutoConfiguration
@Slf4j
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // okhttp连接池
        // 最大空闲连接数200，空闲连接存活时间10s
        ConnectionPool pool = new ConnectionPool(200, 10 * 1000, TimeUnit.SECONDS);
        // 创建okhttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectionPool(pool)
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();
        ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        stringMessageToUtf8(restTemplate);
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new LogInterceptor(2 * 1000));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    /**
     * 修改默认的StringHttpMessageConverter的字符集
     */
    private void stringMessageToUtf8(RestTemplate template) {
        List<HttpMessageConverter<?>> converterList = template.getMessageConverters();
        Optional<HttpMessageConverter<?>> messageConverterOptional = converterList.stream()
                .filter(convert -> convert.getClass().equals(StringHttpMessageConverter.class))
                .findFirst();
        messageConverterOptional.ifPresent(converterList::remove);
        //添加新的StringHttpMessageConverter转换器，并设置字符集为UTF-8
        converterList.add(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    /**
     * 日志拦截器
     */
    @Slf4j
    @Data
    @AllArgsConstructor
    public static class LogInterceptor implements ClientHttpRequestInterceptor {

        /**
         * 告警时间,请求超过这个时间就进行记录
         */
        private int alarmTime;

        @NotNull
        @Override
        public ClientHttpResponse intercept(@NotNull HttpRequest request,
                                            @NotNull byte[] body,
                                            @NotNull ClientHttpRequestExecution execution) throws IOException {
            long startMs = System.currentTimeMillis();
            ClientHttpResponse response = execution.execute(request, body);
            long cost = System.currentTimeMillis() - startMs;
            if (cost > getAlarmTime()) {
                log.error("RestTemplate慢请求,路径: [{}], 耗时: [{}ms]", request.getURI(), cost);
            }
            printLog(request, body, response);
            return response;
        }


        private void printLog(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
            if (log.isDebugEnabled()) {
                String responseStr = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
                log.info(
                        "URI          : {}, \n" +
                                "Method       : {}, \n" +
                                "Headers      : {}, \n" +
                                "Param        : {}, \n" +
                                "RespStatus   : {}, \n" +
                                "Response     : {}",
                        request.getURI(),
                        request.getMethod(),
                        request.getHeaders(),
                        new String(body, StandardCharsets.UTF_8),
                        response.getStatusCode(),
                        responseStr);
            }
        }

    }


}
