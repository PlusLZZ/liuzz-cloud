package com.liuzz.cloud.common.mybatis.processor;

import com.baomidou.dynamic.datasource.processor.DsHeaderProcessor;
import com.liuzz.cloud.common.mybatis.utils.DsUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author liuzz
 */
@Slf4j
public class MyDsHeaderProcessor extends DsHeaderProcessor {
    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        String dsName = super.doDetermineDatasource(invocation, key);
        DsUtil.dynamicCheckDataSource(dsName);
        return dsName;
    }
}
