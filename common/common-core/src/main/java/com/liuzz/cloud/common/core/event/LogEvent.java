package com.liuzz.cloud.common.core.event;

import com.liuzz.cloud.common.core.domain.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 * 日志发布事件
 * @author liuzz
 */
public class LogEvent extends ApplicationEvent {
    public LogEvent(SysLog source) {
        super(source);
    }
}
