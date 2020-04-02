package com.honzooban.questionnairesystem.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName Constant.java
 * @Description 应用中的共享常量
 * @createTime 2019年12月07日 20:27:00
 */
public class Constant {

    /**
     * 常用的参数Map
     */
    public static final HashMap<String, Object> code2SessionParam = new HashMap<>();

    static {
        code2SessionParam.put("appid", Constant.APP_ID);
        code2SessionParam.put("secret", Constant.APP_SECRET);
        code2SessionParam.put("grant_type", Constant.AUTHORIZATION_CODE);
    }

    /**
     * 微信小程序api有关常量
     */
    public static final String APP_ID = "wx3252938b42652bf1";
    public static final String APP_SECRET = "83cc3867b33167723d78bcc5c9aa7860";
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String CLIENT_CREDENTIAL = "client_credential";
    public static final String LOGIN_API = "https://api.weixin.qq.com/sns/jscode2session";
    public static final String ACCESS_TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token";
    public static final String OPEN_ID = "openid";

    /**
     * 请求头参数常量
     */
    public static final String ED_UTF_8 = "UTF-8";
    public static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";


    public static final String STATUS = "status";
    public static final Integer UN_FINISH = 0;
    public static final Integer FINISH = 1;
    public static final Integer ONE_LINE = 1;
    public static final Integer TEST_INPUT = 0;
    public static final double CLASSES_UNANIMOUS = 0.80d;

    public static final String DECATT = "is_like";
    public static final String TRAINING_SET_FILE = "C:/Users/93231/Desktop/questionnaire.arff";
    public static final String DECISION_TREE_FILE = "C:/Users/93231/Desktop/decision_tree.xml";
}
