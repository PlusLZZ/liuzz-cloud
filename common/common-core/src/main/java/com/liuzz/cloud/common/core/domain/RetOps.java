package com.liuzz.cloud.common.core.domain;

import com.liuzz.cloud.common.core.constants.SystemConstant;

import java.util.Optional;

/**
 * @author liuzz
 */
public class RetOps<T> {

    private final Result original;

    public RetOps(Result original) {
        this.original = original;
    }

    public static <T> RetOps<T> of(Result original) {
        return new RetOps<>(original);
    }

    public Result get(){
        return original;
    }

    public boolean isSuccess(){
        if (original == null){
            return false;
        }
        return original.getOrDefault(Result.CODE, SystemConstant.FAIL).equals(SystemConstant.FAIL);
    }

    public String getCode(){
        return (String) original.getOrDefault(Result.CODE, SystemConstant.FAIL);
    }

    public Optional<T> getData(){
        if (original == null){
            return Optional.empty();
        }
        return Optional.ofNullable((T) original.get(Result.DATA));
    }

    public Optional<String> getMsg(){
        if (original == null){
            return Optional.empty();
        }
        return Optional.ofNullable( (String) original.get(Result.MSG));
    }

}
