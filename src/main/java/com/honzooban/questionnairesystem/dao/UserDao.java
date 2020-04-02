package com.honzooban.questionnairesystem.dao;

import com.honzooban.questionnairesystem.dao.model.Question;
import com.honzooban.questionnairesystem.dao.model.User;
import com.honzooban.questionnairesystem.dto.SubmitParam;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName UserDao.java
 * @Description
 * @createTime 2020年03月29日 15:35:00
 */
@Repository
public interface UserDao {

    /**
     * 新增用户信息
     * @param user 用户信息
     * @return
     */
    Integer insertUser(User user);

    /**
     * 获取问题列表
     * @return
     */
    ArrayList<Question> listQuestion();

    /**
     * 插入提交信息（训练集）
     * @param param 提交信息
     * @return
     */
    Integer insertTestInput(SubmitParam param);

    /**
     * 插入提交信息（真实集）
     * @param param 提交信息
     * @return
     */
    Integer insertTestForecast(SubmitParam param);

    /**
     * 修改用户状态信息
     * @param id 用户id
     * @return
     */
    Integer updateUserStatus(Integer id);

    /**
     * 根据用户id查找用户openId
     * @param id 用户id
     * @return
     */
    String selectOpenIdById(Integer id);
}
