package com.liuzz.cloud.common.log.event;


import com.liuzz.cloud.common.log.domain.SysLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 异步监听日志事件
 *
 * @author liuzz
 */
@Slf4j
@RequiredArgsConstructor
public class LogListener {



    @Async
    @Order
    @EventListener(LogEvent.class)
    public void saveSysLog(LogEvent event) {
        SysLog sysLog = (SysLog) event.getSource();
        // TODO 操作日志入库如何操作
    }

}
