package com.honzooban.questionnairesystem.util;

import com.google.common.base.CaseFormat;
import com.honzooban.questionnairesystem.dto.SubmitParam;
import com.honzooban.questionnairesystem.util.vaild.CommonValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName MethodUtil.java
 * @Description 调用某各类指定方法的工具类
 * @createTime 2020年04月03日 14:16:00
 */
public class MethodUtil {

    /**
     * 根据方法名和实例对应Class对象来获取对应方法
     * @param name
     * @return
     */
    public static Method getMethod(String name, Object instance){
        Method method;
        try {
            String methodName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
            return instance.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
