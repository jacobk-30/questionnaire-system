package com.honzooban.questionnairesystem.dto;

import javax.validation.constraints.NotNull;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName LoginParam.java
 * @Description
 * @createTime 2020年03月30日 14:43:00
 */
public class LoginParam {

    @NotNull(message = "code不能为空")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
