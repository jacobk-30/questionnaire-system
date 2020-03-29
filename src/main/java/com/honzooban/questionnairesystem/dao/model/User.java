package com.honzooban.questionnairesystem.dao.model;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName User.java
 * @Description
 * @createTime 2020年03月29日 15:35:00
 */
public class User {

    private Integer id;
    private String openId;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User(){}

    public User(String openId, Integer status) {
        this.openId = openId;
        this.status = status;
    }
}
