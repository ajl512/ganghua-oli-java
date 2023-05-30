package com.gangyunshihua.controller;

import com.gangyunshihua.pojo.GyResult;
import com.gangyunshihua.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "employee", method = RequestMethod.POST)
@ResponseBody
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 查询员工
     */
    @RequestMapping("/findEmployee")
    public GyResult findEmployee(Integer userId, Integer type) throws Exception {
        Integer[] driverStatusArray;
        Integer[] supercargoStatusArray;
        if (type == 0) {
            driverStatusArray = new Integer[]{1, 2};
            supercargoStatusArray = new Integer[]{1, 2};
        } else if (type == 1) {
            driverStatusArray = new Integer[]{1};
            supercargoStatusArray = new Integer[]{2};
        } else {
            driverStatusArray = new Integer[]{2};
            supercargoStatusArray = new Integer[]{1};
        }
        return employeeService.findEmployee(userId, driverStatusArray, supercargoStatusArray);
    }

    /**
     * 新增员工
     */
    @RequestMapping("/addEmployee")
    public GyResult addEmployee(Integer userId, Integer driverStatus, Integer supercargoStatus, String name, String mobile, String idNumber) throws Exception {
        if (driverStatus != 1 && driverStatus != 2) return GyResult.fail("是否司机状态错误");
        if (supercargoStatus != 1 && supercargoStatus != 2) return GyResult.fail("是否押运员状态错误");
        if (StringUtils.isEmpty(name)) return GyResult.fail("姓名不能为空");
//        if (StringUtils.isEmpty(mobile)) return GyResult.fail("手机号不能为空");
        if (StringUtils.isEmpty(idNumber)) return GyResult.fail("身份证号不能为空");
        return employeeService.addEmployee(userId, driverStatus, supercargoStatus, name, mobile, idNumber);
    }

    /**
     * 修改员工
     */
    @RequestMapping("/editEmployee")
    public GyResult editEmployee(Integer userId, Integer employeeId, Integer driverStatus, Integer supercargoStatus, String name, String mobile, String idNumber) throws Exception {
        if (driverStatus != 1 && driverStatus != 2) return GyResult.fail("是否司机状态错误");
        if (supercargoStatus != 1 && supercargoStatus != 2) return GyResult.fail("是否押运员状态错误");
        if (StringUtils.isEmpty(name)) return GyResult.fail("姓名不能为空");
//        if (StringUtils.isEmpty(mobile)) return GyResult.fail("手机号不能为空");
        if (StringUtils.isEmpty(idNumber)) return GyResult.fail("身份证号不能为空");
        return employeeService.editEmployee(userId, employeeId, driverStatus, supercargoStatus, name, mobile, idNumber);
    }

    /**
     * 删除员工
     */
    @RequestMapping("/deleteEmployee")
    public GyResult deleteEmployee(Integer userId, Integer employeeId) throws Exception {
        return employeeService.deleteEmployee(userId, employeeId);
    }
}