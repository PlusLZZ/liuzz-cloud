package com.liuzz.cloud.common.core.utils.servlet;

import cn.hutool.http.HttpUtil;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author liuzz
 */
@UtilityClass
public class ServletUtil {

    /**
     * 获取 HttpServletRequest
     *
     * @return {HttpServletRequest}
     */
    public Optional<HttpServletRequest> getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(requestAttributes).map(ServletRequestAttributes::getRequest);
    }

    /**
     * 获取 HttpServletResponse
     *
     * @return {HttpServletResponse}
     */
    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 请求归属地获取
     * @param request
     */
    public String getIpLocation(HttpServletRequest request){
        // 一般项目会配置反向代理等，所以 x-forwarded-for 有时获取不到 用户真实地址
        // 故而 加上 Proxy-Client-IP，WL-Proxy-Client-IP （此两条为 apache 对 request 的封装）
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String str="http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
        String api = String.format(str, ip);
        return HttpUtil.get(api);
    }

}
