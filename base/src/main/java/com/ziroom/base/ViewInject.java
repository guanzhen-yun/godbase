package com.ziroom.base;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Author:关震
 * Date:2020/4/27 14:54
 * Description:ViewInject 注入layout
 **/
@Retention(RUNTIME) //运行时 注解
@Target(TYPE)//类 接口 注解
public @interface ViewInject {
    int layoutId() default -1;
}
