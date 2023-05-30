package com.gangyunshihua.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gangyunshihua.entity.User;
import com.gangyunshihua.jedis.JedisClient;
import com.gangyunshihua.pojo.Config;
import com.gangyunshihua.pojo.GyResult;
import com.gangyunshihua.repository.UserRepository;
import com.gangyunshihua.service.UserService;
import com.gangyunshihua.utils.HttpClientUtil;
import com.gangyunshihua.utils.ListUtil;
import com.gangyunshihua.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JedisClient jedisClient;

    @Override
    public GyResult login(String code) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + Config.appId + "&secret=" + Config.secret + "&js_code=" + code + "&grant_type=authorization_code";
        String value = HttpClientUtil.getGetValue(url);
        if (value == null) return GyResult.fail("code错误");
        String openId = JSONObject.parseObject(value).getString("openid");
        User user = userRepository.findByOpenId(openId);
        if (user == null) {
            user = new User();
            user.setOpen_id(openId);
            userRepository.save(user);
        }
        JSONObject data = new JSONObject();
        data.put("userId", user.getId());
        data.put("token", getToken(user.getId()));
        data.put("username", user.getUsername());
        data.put("avatar", user.getAvatar());
        data.put("gender", user.getGender());
        data.put("mobile", user.getMobile());
        data.put("company", user.getCompany());
        data.put("isManager", ListUtil.judgeContain(Config.managerIdArray, user.getId()));
        return GyResult.success(data);
    }

    @Override
    public GyResult editUser(Integer userId, String username, String avatar, Integer gender, String mobile, String company) throws Exception {
        User user = userRepository.findOne(userId);
        user.setUsername(username);
        user.setAvatar(avatar);
        user.setGender(gender);
        if (StringUtils.isNotEmpty(mobile)) user.setMobile(mobile);
        if (StringUtils.isNotEmpty(company)) user.setCompany(company);
        userRepository.save(user);
        return GyResult.success();
    }

    @Override
    public GyResult findUserInfo(Integer userId) throws Exception {
        User user = userRepository.findOne(userId);
        JSONObject data = new JSONObject();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("avatar", user.getAvatar());
        data.put("gender", user.getGender());
        data.put("mobile", user.getMobile());
        data.put("company", user.getCompany());
        return GyResult.success(data);
    }

    private String getToken(Integer userId) throws Exception {
        String token = StringUtil.getMD5(StringUtil.getRandomString(12));
        jedisClient.set(userId + "_gyToken", token);
        jedisClient.expire(userId + "_gyToken", 36000);
        return token;
    }
}