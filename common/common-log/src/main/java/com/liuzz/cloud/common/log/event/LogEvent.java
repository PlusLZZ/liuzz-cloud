package com.liuzz.cloud.common.log.event;

import com.liuzz.cloud.common.log.domain.SysLog;
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
