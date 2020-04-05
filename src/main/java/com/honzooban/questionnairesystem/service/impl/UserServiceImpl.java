package com.honzooban.questionnairesystem.service.impl;

import com.honzooban.questionnairesystem.common.Constant;
import com.honzooban.questionnairesystem.dao.UserDao;
import com.honzooban.questionnairesystem.dao.model.Question;
import com.honzooban.questionnairesystem.dao.model.User;
import com.honzooban.questionnairesystem.dto.SubmitParam;
import com.honzooban.questionnairesystem.service.UserService;
import com.honzooban.questionnairesystem.util.HttpUtil;
import com.honzooban.questionnairesystem.util.Id3Util;
import com.honzooban.questionnairesystem.util.RedisUtil;
import com.honzooban.questionnairesystem.util.vaild.CommonValidator;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName UserServiceImpl.java
 * @Description
 * @createTime 2020年03月29日 15:40:00
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserDao userDao;
    @Autowired
    Id3Util id3Util;

    @Override
    public User login(String code) {
        Map<String, Object> code2SessionParam = Constant.code2SessionParam;
        code2SessionParam.put("js_code", code);
        JSONObject object = HttpUtil.doGet(Constant.LOGIN_API, code2SessionParam);
        String openId = (String) object.get(Constant.OPEN_ID);
        if (CommonValidator.notNull(openId)) {
            User user = (User) redisUtil.selectCache(openId, User.class);
            if (CommonValidator.notNull(user)) {
                return user;
            }
        }
        User user = new User(openId, Constant.UN_FINISH);
        if (CommonValidator.isProcessSuccess(userDao.insertUser(user), Constant.ONE_LINE)) {
            redisUtil.insertCache(openId, user);
            return user;
        }
        return null;
    }

    @Override
    public ArrayList<Question> listQuestion() {
        return userDao.listQuestion();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitTestQuestionnaire(SubmitParam param) {
        // 获取用户openId
        String openId = userDao.selectOpenIdById(param.getUid());
        // 检查用户是否已经提交过问卷
        if (CommonValidator.notNull(openId)) {
            User user = (User) redisUtil.selectCache(openId, User.class);
            if (CommonValidator.notNull(user)) {
                if (user.getStatus().equals(Constant.FINISH)) {
                    return false;
                }
            }
        }
        if (CommonValidator.isProcessSuccess(userDao.insertTestInput(param), Constant.ONE_LINE)) {
            if (CommonValidator.isProcessSuccess(userDao.updateUserStatus(param.getUid()), Constant.ONE_LINE)) {
                // 修改用户的status为已提交
                redisUtil.updateCache(openId, Constant.STATUS, Constant.FINISH);
                return true;
            }
            return false;
        }
        return false;

    }

    @Override
    public Integer submitForecastQuestionnaire(SubmitParam param) {
        // 获取用户openId
        String openId = userDao.selectOpenIdById(param.getUid());
        // 检查用户是否已经提交过问卷
        if (CommonValidator.notNull(openId)) {
            User user = (User) redisUtil.selectCache(openId, User.class);
            if (CommonValidator.notNull(user)) {
                if (user.getStatus().equals(Constant.FINISH)) {
                    return -1;
                }
            }
        }
        if (CommonValidator.isProcessSuccess(userDao.insertTestForecast(param), Constant.ONE_LINE)) {
            if (CommonValidator.isProcessSuccess(userDao.updateUserStatus(param.getUid()), Constant.ONE_LINE)) {
                // 修改用户的status为已提交
                redisUtil.updateCache(openId, Constant.STATUS, Constant.FINISH);
                return id3Util.getPredictedResult(param);
            }
            return -1;
        }
        return -1;
    }
}
