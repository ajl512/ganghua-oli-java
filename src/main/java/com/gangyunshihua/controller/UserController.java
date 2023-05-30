package com.gangyunshihua.controller;

import com.alibaba.fastjson.JSONObject;
import com.gangyunshihua.pojo.GyResult;
import com.gangyunshihua.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "user", method = RequestMethod.POST)
@ResponseBody
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询是否需要隐藏审核
     */
    @RequestMapping("/findHideCheck")
    public GyResult findHideCheck(String key) throws Exception {
        JSONObject data = new JSONObject();
        if ("asdjhaojwsd".equals(key)) data.put("hideCheck", true);
        else data.put("hideCheck", false);
        return GyResult.success(data);
    }

    /**
     * 登陆
     */
    @RequestMapping("/login")
    public GyResult login(String code) throws Exception {
        if (StringUtils.isEmpty(code)) return GyResult.fail("code不能为空");
        return userService.login(code);
    }

    /**
     * 修改用户信息
     */
    @RequestMapping("/editUser")
    public GyResult editUser(Integer userId, String username, String avatar, Integer gender, String mobile, String company) throws Exception {
        if (StringUtils.isEmpty(username)) return GyResult.fail("昵称不能为空");
        if (StringUtils.isEmpty(avatar)) return GyResult.fail("头像不能为空");
        if (gender != 0 && gender != 1 && gender != 2) return GyResult.fail("性别错误");
        return userService.editUser(userId, username, avatar, gender, mobile, company);
    }

    /**
     * 查询用户信息
     */
    @RequestMapping("/findUserInfo")
    public GyResult findUserInfo(Integer userId) throws Exception {
        return userService.findUserInfo(userId);
    }
}