package com.liuzz.cloud.common.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import javax.annotation.PostConstruct;

/**
 * mongodb自动配置
 *
 * @author liuzz
 */
@AutoConfiguration
@RequiredArgsConstructor
public class MongoDbAutoConfiguration {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void init() {
        final DefaultMongoTypeMapper defaultMongoTypeMapper = new DefaultMongoTypeMapper(null);
        ((MappingMongoConverter) mongoTemplate.getConverter()).setTypeMapper(defaultMongoTypeMapper);
    }


}
