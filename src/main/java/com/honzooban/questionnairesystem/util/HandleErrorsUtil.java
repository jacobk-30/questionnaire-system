package com.honzooban.questionnairesystem.util;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName HandleErrorsUtil.java
 * @Description 处理数据格式检验异常工具类
 * @createTime 2019年12月07日 23:27:00
 */
public class HandleErrorsUtil {

    /**
     * 处理进入控制层前数据校验返回的Errors对象
     * @param errors Errors对象
     * @return 存储错误信息的Map集合
     */
    public static Map<String, String> handleErrors(Errors errors){
        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            Map<String, String> errorsMap = new HashMap<>();
            for(ObjectError objectError : allErrors){
                if(objectError instanceof FieldError){
                    FieldError fe = (FieldError) objectError;
                    errorsMap.put(fe.getField(), fe.getDefaultMessage());
                }else{
                    errorsMap.put(objectError.getObjectName(), objectError.getDefaultMessage());
                }
            }
            return errorsMap;
        }
        return null;
    }
}
