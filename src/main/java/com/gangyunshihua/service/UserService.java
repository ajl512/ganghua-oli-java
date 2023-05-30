package com.gangyunshihua.service;

import com.gangyunshihua.pojo.GyResult;

public interface UserService {

    GyResult login(String code) throws Exception;

    GyResult editUser(Integer userId, String username, String avatar, Integer gender, String mobile, String company) throws Exception;

    GyResult findUserInfo(Integer userId) throws Exception;
}
