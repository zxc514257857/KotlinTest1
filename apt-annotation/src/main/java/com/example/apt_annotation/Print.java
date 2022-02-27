package com.example.apt_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解lib中创建一个注解类
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Print {
} 