package com.gangyunshihua.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gangyunshihua.entity.Employee;
import com.gangyunshihua.pojo.GyResult;
import com.gangyunshihua.repository.EmployeeRepository;
import com.gangyunshihua.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public GyResult findEmployee(Integer userId, Integer[] driverStatusArray, Integer[] supercargoStatusArray) throws Exception {
        JSONArray data = new JSONArray();
        for (Employee employee : employeeRepository.findEmployee(userId, driverStatusArray, supercargoStatusArray)) {
            JSONObject object = new JSONObject();
            object.put("employeeId", employee.getId());
            object.put("driverStatus", employee.getDriver_status());
            object.put("supercargoStatus", employee.getSupercargo_status());
            object.put("name", employee.getName());
            object.put("mobile", employee.getMobile());
            object.put("idNumber", employee.getId_number());
            data.add(object);
        }
        return GyResult.success(data);
    }

    @Override
    public GyResult addEmployee(Integer userId, Integer driverStatus, Integer supercargoStatus, String name, String mobile, String idNumber) throws Exception {
        Employee employee = new Employee();
        employee.setUser_id(userId);
        employee.setDriver_status(driverStatus);
        employee.setSupercargo_status(supercargoStatus);
        employee.setName(name);
        employee.setMobile(StringUtils.isEmpty(mobile) ? "" : mobile);
        employee.setId_number(idNumber);
        employeeRepository.save(employee);
        return GyResult.success();
    }

    @Override
    public GyResult editEmployee(Integer userId, Integer employeeId, Integer driverStatus, Integer supercargoStatus, String name, String mobile, String idNumber) throws Exception {
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null || employee.getStatus() == 2) return GyResult.fail("该员工不存在");
        if (employee.getUser_id() != userId.intValue()) return GyResult.fail("该员工不是你的员工");
        employee.setDriver_status(driverStatus);
        employee.setSupercargo_status(supercargoStatus);
        employee.setName(name);
        employee.setMobile(StringUtils.isEmpty(mobile) ? "" : mobile);
        employee.setId_number(idNumber);
        employeeRepository.save(employee);
        return GyResult.success();
    }

    @Override
    public GyResult deleteEmployee(Integer userId, Integer employeeId) throws Exception {
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null || employee.getStatus() == 2) return GyResult.fail("该员工不存在");
        if (employee.getUser_id() != userId.intValue()) return GyResult.fail("该员工不是你的员工");
        employee.setStatus(2);
        employeeRepository.save(employee);
        return GyResult.success();
    }
}