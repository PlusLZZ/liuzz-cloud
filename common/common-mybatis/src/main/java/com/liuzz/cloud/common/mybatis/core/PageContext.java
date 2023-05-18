package com.liuzz.cloud.common.mybatis.core;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author liuzz
 */
public class PageContext {

    public static final String PAGE_SIZE = "pageSize";

    public static final String PAGE_NUM = "pageNum";


    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 获取分页对象
     */
    public static <T> Page<T> getPage() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new Page<>(1, 10);
        }
        final HttpServletRequest request = requestAttributes.getRequest();
        long size = 10;
        long current = 1;
        try {
            size = Optional.ofNullable(ServletRequestUtils.getLongParameter(request, PAGE_SIZE)).orElse(10L);
            current = Optional.ofNullable(ServletRequestUtils.getLongParameter(request, PAGE_NUM)).orElse(1L);
        } catch (Exception ignore) {
        }
        return Page.of(current, size);
    }

}
