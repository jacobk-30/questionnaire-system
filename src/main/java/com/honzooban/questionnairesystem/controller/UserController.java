package com.honzooban.questionnairesystem.controller;

import com.honzooban.questionnairesystem.common.CommonResult;
import com.honzooban.questionnairesystem.common.ResultCodeEnum;
import com.honzooban.questionnairesystem.dao.model.Question;
import com.honzooban.questionnairesystem.dao.model.User;
import com.honzooban.questionnairesystem.dto.SubmitParam;
import com.honzooban.questionnairesystem.service.UserService;
import com.honzooban.questionnairesystem.util.HandleErrorsUtil;
import com.honzooban.questionnairesystem.util.vaild.CommonValidator;
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

    @Autowired
    UserService userService;

    /**
     * 用户登录
     * @param code 登录凭证
     * @return 登录结果
     */
    @GetMapping("login/{code}")
    public CommonResult login(@PathVariable("code") String code){
        User user = userService.login(code);
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
     * 提交问卷
     * @param param 提交信息
     * @return 提交结果
     */
    @PostMapping("submitQuestionnaire")
    public CommonResult submitQuestionnaire(@RequestBody @Valid SubmitParam param, Errors errors){
        Map<String, String> errorsMap = HandleErrorsUtil.handleErrors(errors);
        if(CommonValidator.notNull(errorsMap)){
            return CommonResult.failed(ResultCodeEnum.VALIDATE_FAILED, errorsMap);
        }
        boolean result = userService.submitQuestionnaire(param);
        return result? CommonResult.success("提交成功"):
                CommonResult.failed(ResultCodeEnum.FAILED, "提交信息有误");
    }

}