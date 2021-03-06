package com.yuntun.sanitationkitchen.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 权限控制
 * @author whj
 * @since 2020/11/30
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Limit {

    String [] value() default {};


}
