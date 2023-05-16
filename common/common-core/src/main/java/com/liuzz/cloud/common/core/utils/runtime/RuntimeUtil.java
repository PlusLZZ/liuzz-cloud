package com.liuzz.cloud.common.core.utils.runtime;

import lombok.experimental.UtilityClass;

/**
 * 系统运行时工具类
 * @author liuzz
 */
@UtilityClass
public class RuntimeUtil {

    /**
     * 获取CPU的核心数量
     * @return 16 核
     */
    public int getCpuCore(){
        return Runtime.getRuntime().availableProcessors();
    }

}
