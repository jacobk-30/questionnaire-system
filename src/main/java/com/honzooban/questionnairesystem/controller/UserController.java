package com.honzooban.questionnairesystem.controller;

import afu.org.checkerframework.checker.units.qual.A;
import com.honzooban.questionnairesystem.common.CommonResult;
import com.honzooban.questionnairesystem.common.ResultCodeEnum;
import com.honzooban.questionnairesystem.dao.model.Question;
import com.honzooban.questionnairesystem.dao.model.User;
import com.honzooban.questionnairesystem.dto.LoginParam;
import com.honzooban.questionnairesystem.dto.SubmitParam;
import com.honzooban.questionnairesystem.service.UserService;
import com.honzooban.questionnairesystem.util.HandleErrorsUtil;
import com.honzooban.questionnairesystem.util.Id3Util;
import com.honzooban.questionnairesystem.util.vaild.CommonValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName UserController.java
 * @Description
 * @createTime 2020年03月29日 15:32:00
 */
@RestController
@RequestMapping("user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    /**
     * 用户登录
     * @param param 登录信息
     * @return 登录结果
     */
    @PostMapping("login")
    public CommonResult login(@RequestBody @Valid LoginParam param, Errors errors){
        Map<String, String> errorsMap = HandleErrorsUtil.handleErrors(errors);
        if(CommonValidator.notNull(errorsMap)){
            return CommonResult.failed(ResultCodeEnum.VALIDATE_FAILED, errorsMap);
        }
        User user = userService.login(param.getCode());
        return CommonValidator.notNull(user)? CommonResult.success("登录成功", user):
                CommonResult.failed(ResultCodeEnum.FAILED, "请求参数有误");
    }

    /**
     * 获取问题列表
     * @return 问题列表
     */
    @GetMapping("listQuestion")
    public CommonResult listQuestion(){
        ArrayList<Question> questionList = userService.listQuestion();
        return CommonValidator.notNull(questionList)? CommonResult.success("获取问题列表成功", questionList):
                CommonResult.failed(ResultCodeEnum.FAILED, "获取问题列表失败，请重试");
    }

    /**
     * 提交问卷（训练）
     * @param param 提交信息
     * @return 提交结果
     */
    @PostMapping("submitTestQuestionnaire")
    public CommonResult submitTestQuestionnaire(@RequestBody @Valid SubmitParam param, Errors errors){
        Map<String, String> errorsMap = HandleErrorsUtil.handleErrors(errors);
        if(CommonValidator.notNull(errorsMap)){
            return CommonResult.failed(ResultCodeEnum.VALIDATE_FAILED, errorsMap);
        }
        boolean result = userService.submitTestQuestionnaire(param);
        return result? CommonResult.success("提交成功"):
                CommonResult.failed(ResultCodeEnum.FAILED, "该账号已提交过问卷或提交频繁，请重试");

    }


    /**
     * 提交问卷（预测）
     * @param param 提交信息
     * @return 提交结果
     */
    @PostMapping("submitForecastQuestionnaire")
    public CommonResult submitForecastQuestionnaire(@RequestBody @Valid SubmitParam param, Errors errors){
        Map<String, String> errorsMap = HandleErrorsUtil.handleErrors(errors);
        if(CommonValidator.notNull(errorsMap)){
            return CommonResult.failed(ResultCodeEnum.VALIDATE_FAILED, errorsMap);
        }
        Integer result = userService.submitForecastQuestionnaire(param);
        if(!CommonValidator.notNull(result)){
            logger.error("用户id：" + param.getUid() + " 预测结果为空");
            return CommonResult.failed(ResultCodeEnum.FAILED, "预测失败，模型属实不行");
        }
        logger.error("用户id：" + param.getUid() + "预测结果为：" + result);
        return result == -1? CommonResult.failed(ResultCodeEnum.FAILED, "该账号已提交过问卷或提交频繁，请重试"):
                CommonResult.success("提交成功", result);

    }
}
