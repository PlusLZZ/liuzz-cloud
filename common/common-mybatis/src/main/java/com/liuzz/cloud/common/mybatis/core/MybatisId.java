package com.liuzz.cloud.common.mybatis.core;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Sequence;

import java.net.InetAddress;

/**
 * 定义雪花算法的ID生成规则
 * 一般需要指定工作机器ID与序列号,不然有几率生成重复的ID
 * 在docker环境下,由于容器内主机的网络信息在同一个宿主机内不同,大部分情况下也可以保证唯一
 * @author liuzz
 */
public class MybatisId implements IdentifierGenerator {

    private final Sequence sequence;

    public MybatisId() {
        this.sequence = new Sequence(null);
    }

    public MybatisId(InetAddress inetAddress) {
        this.sequence = new Sequence(inetAddress);
    }

    /**
     * 这两个ID分别根据host与ip生成,在容器多实例的情况下,大部分情况可以保持唯一
     * 也可以选择使用redis在项目启动的时候来自动分配
     * 由于目前业务还不存在多机房部署的规格,可以固定dataCenterId,workerId在项目启动时向redis注册,项目销毁时向redis销毁
     * @param workerId      类比于机器ID
     * @param dataCenterId  类比于机房ID
     */
    public MybatisId(long workerId, long dataCenterId) {
        this.sequence = new Sequence(workerId, dataCenterId);
    }

    public MybatisId(Sequence sequence) {
        this.sequence = sequence;
    }


    @Override
    public Number nextId(Object entity) {
        return sequence.nextId();
    }

}
