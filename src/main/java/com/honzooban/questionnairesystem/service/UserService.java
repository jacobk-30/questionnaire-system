package com.honzooban.questionnairesystem.service;

import com.honzooban.questionnairesystem.dao.model.Question;
import com.honzooban.questionnairesystem.dao.model.User;
import com.honzooban.questionnairesystem.dto.SubmitParam;

import java.util.ArrayList;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName UserService.java
 * @Description
 * @createTime 2020年03月29日 15:40:00
 */
public interface UserService {

    /**
     * 用户登录返回结果
     * @param code 登录凭证
     * @return 登录结果
     */
    User login(String code);

    /**
     * 获取所有的问题
     * @return 问题集合
     */
    ArrayList<Question> listQuestion();

    /**
     * 提交问卷
     * @param param 提交信息
     * @return 提交结果
     */
    boolean submitQuestionnaire(SubmitParam param);
}
