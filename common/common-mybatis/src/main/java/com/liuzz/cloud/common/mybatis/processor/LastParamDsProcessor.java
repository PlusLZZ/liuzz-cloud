package com.liuzz.cloud.common.mybatis.processor;


import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.liuzz.cloud.common.mybatis.utils.DsUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;


/**
 * 添加参数数据源解析
 * 检测到@DS("#last) 将会清除栈中所有数据源变量,同时本次数据源使用方法参数中最后一个变量的值为key
 * @author liuzz
 */
@Slf4j
public class LastParamDsProcessor extends DsProcessor {

	private static final String LAST_PREFIX = "#last";

	/**
	 * 抽象匹配条件 匹配才会走当前执行器否则走下一级执行器
	 * @param key DS注解里的内容
	 * @return 是否匹配
	 */
	@Override
	public boolean matches(String key) {
		if (key.startsWith(LAST_PREFIX)) {
			DynamicDataSourceContextHolder.clear();
			return true;
		}
		return false;
	}

	/**
	 * 抽象最终决定数据源
	 * @param invocation 方法执行信息
	 * @param key DS注解里的内容
	 * @return 数据源名称
	 */
	@Override
	public String doDetermineDatasource(MethodInvocation invocation, String key) {
		Object[] arguments = invocation.getArguments();
		String dsName = String.valueOf(arguments[arguments.length - 1]);
		DsUtil.dynamicCheckDataSource(dsName);
		return dsName;
	}

}
