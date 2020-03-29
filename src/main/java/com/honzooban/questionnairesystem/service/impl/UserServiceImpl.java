package com.honzooban.questionnairesystem.service.impl;

import com.honzooban.catrecruit.commoms.Constant;
import com.honzooban.questionnairesystem.dao.UserDao;
import com.honzooban.questionnairesystem.dao.model.Question;
import com.honzooban.questionnairesystem.dao.model.User;
import com.honzooban.questionnairesystem.dto.SubmitParam;
import com.honzooban.questionnairesystem.service.UserService;
import com.honzooban.questionnairesystem.util.HttpUtil;
import com.honzooban.questionnairesystem.util.RedisUtil;
import com.honzooban.questionnairesystem.util.vaild.CommonValidator;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Override
    public User login(String code) {
        Map<String, Object> code2SessionParam = Constant.code2SessionParam;
        code2SessionParam.put("js_code", code);
        JSONObject object = HttpUtil.doGet(Constant.LOGIN_API, code2SessionParam);
        String openId = (String) object.get(Constant.OPEN_ID);
        if(!CommonValidator.notNull(openId)){
            return null;
        }
        User user = new User(openId, Constant.UN_FINISH);
        if(CommonValidator.isProcessSuccess(userDao.insertUser(user), Constant.ONE_LINE)){
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
    public boolean submitQuestionnaire(SubmitParam param) {
        if(param.getStatus().equals(Constant.TEST_INPUT)){
            return CommonValidator.isProcessSuccess(userDao.insertTestInput(param), Constant.ONE_LINE);
        }
        return CommonValidator.isProcessSuccess(userDao.insertTestForecast(param), Constant.ONE_LINE);
    }

}
