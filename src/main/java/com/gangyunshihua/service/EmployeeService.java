package com.gangyunshihua.service;

import com.gangyunshihua.pojo.GyResult;

public interface EmployeeService {

    GyResult findEmployee(Integer userId, Integer[] driverStatusArray, Integer[] supercargoStatusArray) throws Exception;

    GyResult addEmployee(Integer userId, Integer driverStatus, Integer supercargoStatus, String name, String mobile, String idNumber) throws Exception;

    GyResult editEmployee(Integer userId, Integer employeeId, Integer driverStatus, Integer supercargoStatus, String name, String mobile, String idNumber) throws Exception;

    GyResult deleteEmployee(Integer userId, Integer employeeId) throws Exception;
}
