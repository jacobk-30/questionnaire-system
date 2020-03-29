package com.honzooban.questionnairesystem.dao.model;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName Question.java
 * @Description
 * @createTime 2020年03月29日 18:33:00
 */
public class Question {

    private Integer id;
    private String question;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
