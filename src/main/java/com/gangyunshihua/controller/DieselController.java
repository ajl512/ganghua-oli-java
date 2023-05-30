package com.gangyunshihua.controller;

import com.gangyunshihua.pojo.Config;
import com.gangyunshihua.pojo.GyResult;
import com.gangyunshihua.service.DieselService;
import com.gangyunshihua.utils.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "diesel", method = RequestMethod.POST)
@ResponseBody
public class DieselController {

    @Autowired
    private DieselService dieselService;

    /**
     * 查询柴油价格
     */
    @RequestMapping("/findDiesel")
    public GyResult findDiesel(Integer userId) throws Exception {
        return dieselService.findDiesel(userId);
    }

    /**
     * 查询柴油历史价格
     */
    @RequestMapping("/findDieselLog")
    public GyResult findDieselLog(Integer dieselId) throws Exception {
        return dieselService.findDieselLog(dieselId);
    }

    /**
     * 查询预设的柴油价格
     */
    @RequestMapping("/findDieselFuture")
    public GyResult findDieselFuture(Integer userId) throws Exception {
        if (!ListUtil.judgeContain(Config.managerIdArray, userId)) return GyResult.fail("你没有权限查询预设的柴油价格");
        return dieselService.findDieselFuture();
    }

    /**
     * 设置柴油价格
     */
    @RequestMapping("/editDiesel")
    public GyResult editDiesel(Integer userId, String dieselJson, String validTime) throws Exception {
        if (!ListUtil.judgeContain(Config.managerIdArray, userId)) return GyResult.fail("你没有权限设置柴油价格");
        if (StringUtils.isEmpty(dieselJson)) return GyResult.fail("柴油价格数据不能为空");
        if (StringUtils.isEmpty(validTime)) return dieselService.editDiesel(dieselJson);
        else return dieselService.editDieselFuture(dieselJson, validTime);
    }
}