package com.gangyunshihua.service;

import com.gangyunshihua.pojo.GyResult;

public interface DieselService {

    GyResult findDiesel(Integer userId) throws Exception;

    GyResult findDieselLog(Integer dieselId) throws Exception;

    GyResult findDieselFuture() throws Exception;

    GyResult editDiesel(String dieselJson) throws Exception;

    GyResult editDieselFuture(String dieselJson, String validTime) throws Exception;

    void addDieselLog() throws Exception;

    void flushValid() throws Exception;
}
