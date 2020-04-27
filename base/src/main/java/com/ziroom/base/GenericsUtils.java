package com.ziroom.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Author:关震
 * Date:2020/4/27 14:06
 * Description:GenericsUtils 泛型工具类
 **/
public class GenericsUtils {

    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 获取泛型T class的类型
     */

    public static Class getSuperClassGenricType(Class clazz, int index) throws IndexOutOfBoundsException {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }
}
