package com.liuzz.cloud.common.mongo.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * mongo工具类
 *
 * @author liuzz
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MongoUtils<T> {

    private final MongoTemplate mongoTemplate;

    /**
     * 将对象转换为Bson对象
     */
    public static BSONObject toBson(Object obj) {
        final BSONObject bsonObject = new BasicBSONObject();
        if (obj != null) {
            final Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    final org.springframework.data.mongodb.core.mapping.Field annotation = AnnotationUtils.getAnnotation(field, org.springframework.data.mongodb.core.mapping.Field.class);
                    field.setAccessible(true);
                    final Object o = field.get(obj);
                    if (annotation != null) {
                        bsonObject.put(annotation.name(), o);
                    } else {
                        bsonObject.put(field.getName(), o);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("toBson Error field:" + field.getName() + obj);
                }
            }
        }
        return bsonObject;
    }

    /**
     * 根据mongo注解等信息,解析bson到类对象
     * 注意类必须要有空构造函数
     */
    public static <T> T toObject(BSONObject bsonObject, Class<T> type) {
        T t;
        try {
            t = type.newInstance();
        } catch (Exception e) {
            log.error("toObject Error", e);
            return null;
        }
        Map<String, Field> fieldMap = new HashMap<>(16);
        for (Field field : type.getDeclaredFields()) {
            final org.springframework.data.mongodb.core.mapping.Field annotation = AnnotationUtils.getAnnotation(field, org.springframework.data.mongodb.core.mapping.Field.class);
            if (annotation != null) {
                fieldMap.put(annotation.name(), field);
            }
            fieldMap.put(field.getName(), field);
        }
        for (String key : bsonObject.keySet()) {
            final Field field = fieldMap.get(key);
            if (field != null) {
                field.setAccessible(true);
                try {
                    field.set(t, bsonObject.get(key));
                } catch (IllegalAccessException ignore) {
                }
            }
        }
        return t;
    }

    /**
     * 根据include生成基础的update语句
     *
     * @param bsonObject bson对象
     * @param mode       生成模式,是否覆盖空值 true-值为空依然更新
     * @param mode       需要更新的字段名
     */
    public static Update updateForBsonInclude(BSONObject bsonObject, boolean mode, String... includes) {
        Update update = new Update();
        for (String include : includes) {
            final Object o = bsonObject.get(include);
            if (o == null && !mode) {
                continue;
            }
            update.set(include, o);
        }
        return update;
    }

    /**
     * 根据include生成基础的update语句
     * 使用字段名即可,会自动根据Field注解返回字段
     *
     * @param mode 生成模式,是否覆盖空值 true-值为空依然更新
     */
    public static Update updateForObjectInclude(Object object, boolean mode, String... includes) {
        Update update = new Update();
        for (String include : includes) {
            try {
                final Field field = object.getClass().getDeclaredField(include);
                field.setAccessible(true);
                final Object o = field.get(object);
                if (o == null && !mode) {
                    continue;
                }
                final org.springframework.data.mongodb.core.mapping.Field annotation = AnnotationUtils.getAnnotation(field, org.springframework.data.mongodb.core.mapping.Field.class);
                if (annotation != null) {
                    update.set(annotation.name(), o);
                } else {
                    update.set(field.getName(), o);
                }
            } catch (Exception e) {
                log.error("updateForObjectInclude error", e);
            }
        }
        return update;
    }

    /**
     * 根据exclude生成基础的update语句
     *
     * @param mode 生成模式,是否覆盖空值 true-值为空依然更新
     */
    public static Update updateForBsonExclude(BSONObject bsonObject, boolean mode, String... excludes) {
        Update update = new Update();
        key:
        for (String key : bsonObject.keySet()) {
            final Object o = bsonObject.get(key);
            if (o == null && !mode) {
                continue;
            }
            for (String exclude : excludes) {
                if (key.equalsIgnoreCase(exclude)) {
                    continue key;
                }
            }
            update.set(key, o);
        }
        return update;
    }

    /**
     * 根据exclude生成基础的update语句
     * 使用字段名即可,会自动根据Field注解返回字段
     *
     * @param mode 生成模式,是否覆盖空值 true-值为空依然更新
     */
    public static Update updateForObjectExclude(Object object, boolean mode, String... excludes) {
        Update update = new Update();
        key:
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object o = field.get(object);
                if (o == null && !mode) {
                    continue;
                }
                final org.springframework.data.mongodb.core.mapping.Field annotation = AnnotationUtils.getAnnotation(field, org.springframework.data.mongodb.core.mapping.Field.class);
                for (String exclude : excludes) {
                    if (field.getName().equalsIgnoreCase(exclude)) {
                        continue key;
                    } else if (annotation != null && annotation.name().equalsIgnoreCase(exclude)) {
                        continue key;
                    }
                }
                update.set(annotation != null ? annotation.name() : field.getName(), o);
            } catch (Exception e) {
                log.error(" updateForObjectExclude error", e);
            }
        }
        return update;
    }

    /**
     * 获取mongo分页对象
     * -1是因为mongo分页索引是从0开始的
     *
     * @param current
     * @param size
     * @return
     */
    public static Pageable toPage(long current, long size) {
        return PageRequest.of((int) current - 1,
                (int) size);
    }

    /**
     * 不区分大小写的模糊查询
     *
     * @param fieldName
     * @return
     */
    public static Pattern like(String fieldName) {
        return Pattern.compile("^.*" + fieldName + ".*$", Pattern.CASE_INSENSITIVE);
    }

}
