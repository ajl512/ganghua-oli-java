package com.gangyunshihua.service;

import com.gangyunshihua.pojo.GyResult;

public interface OrderService {

    GyResult addOrder(Integer userId, String dieselJson, String mobile, String company, String carNumber, Float carLoad, Integer driverId, Integer supercargoId) throws Exception;

    GyResult addOrder(Integer userId, String dieselJson, String mobile, String company) throws Exception;

    GyResult findMyOrder(Integer userId, Integer[] orderStatusArray, Integer page) throws Exception;

    GyResult findMyLastOrder(Integer userId) throws Exception;

    GyResult findOrder(Integer[] orderStatusArray, Integer page) throws Exception;

    GyResult uploadOrderProve(Integer orderId, String prove) throws Exception;

    GyResult completeOrder(Integer orderId) throws Exception;

    GyResult closeOrder(Integer orderId) throws Exception;

    GyResult exportOrder() throws Exception;
}
