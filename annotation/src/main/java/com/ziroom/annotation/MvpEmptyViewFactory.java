package com.ziroom.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * mvp View层为空时的注解
 */

@Retention(CLASS) //编译期 注解
@Target(TYPE)//类 接口 注解
public @interface MvpEmptyViewFactory {
}
