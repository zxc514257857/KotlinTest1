package com.example.case7;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义zhr注解
 * 如果想要对注解进行限定，就需要使用到元注解，就是注解的注解
 * java中有四个元注解：@Target   @Retention   @Documented   @Inherited
 * 1.@Target 用于描述注解的定义范围，可以限制这个注解定义的元素类型，如@Target(ElementType.METHOD,ElementType.FIELD)等
 * 2.@Retention 用于定义注解的生命周期，如@Retention(RetentionPolicy.CLASS,RetentionPolicy.RUNTIME)
 * 其他两个元注解使用得不多，暂不解释
 */
// FIELD表示可以应用于字段和属性，METHOD表示可以应用于方法
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface zhr {

    // 给注解定义参数
    String name() default "王冰冰";

    int age() default 18;
}