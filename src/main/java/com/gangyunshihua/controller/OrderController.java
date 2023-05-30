package com.gangyunshihua.controller;

import com.gangyunshihua.pojo.Config;
import com.gangyunshihua.pojo.GyResult;
import com.gangyunshihua.service.OrderService;
import com.gangyunshihua.utils.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping(value = "order", method = RequestMethod.POST)
@ResponseBody
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 新增订单
     */
    @RequestMapping("/addOrder")
    public GyResult addOrder(Integer userId, Integer pickMode, String dieselJson, String mobile, String company, String carNumber, Float carLoad, Integer driverId, Integer supercargoId) throws Exception {
        if (pickMode != 1 && pickMode != 2) return GyResult.fail("取货方式错误");
        if (StringUtils.isEmpty(dieselJson)) return GyResult.fail("油品不能为空");
        if (StringUtils.isEmpty(mobile)) return GyResult.fail("联系电话不能为空");
        if (StringUtils.isEmpty(company)) return GyResult.fail("公司名称不能为空");
        if (pickMode == 1) {
            if (StringUtils.isEmpty(carNumber)) return GyResult.fail("车牌号不能为空");
            if (carLoad <= 0) return GyResult.fail("车辆核载必须大于0");
            if (driverId == null || driverId == 0) return GyResult.fail("驾驶员不能为空");
            if (supercargoId == null || supercargoId == 0) return GyResult.fail("押运员不能为空");
            return orderService.addOrder(userId, dieselJson, mobile, company, carNumber, carLoad, driverId, supercargoId);
        } else {
            return orderService.addOrder(userId, dieselJson, mobile, company);
        }
    }

    /**
     * 查询我的订单
     */
    @RequestMapping("/findMyOrder")
    public GyResult findMyOrder(Integer userId, Integer orderStatus, Integer page) throws Exception {
        Integer[] orderStatusArray = orderStatus == 0 ? new Integer[]{1, 2} : new Integer[]{orderStatus};
        return orderService.findMyOrder(userId, orderStatusArray, page);
    }

    /**
     * 查询我上一个订单信息
     */
    @RequestMapping("/findMyLastOrder")
    public GyResult findMyLastOrder(Integer userId) throws Exception {
        return orderService.findMyLastOrder(userId);
    }

    /**
     * 查询所有订单
     */
    @RequestMapping("/findOrder")
    public GyResult findOrder(Integer userId, Integer orderStatus, Integer page) throws Exception {
        if (!ListUtil.judgeContain(Config.managerIdArray, userId)) return GyResult.fail("你没有权限查询所有订单");
        Integer[] orderStatusArray = orderStatus == 0 ? new Integer[]{1, 2} : new Integer[]{orderStatus};
        return orderService.findOrder(orderStatusArray, page);
    }

    /**
     * 上传已完成凭证
     */
    @RequestMapping("/uploadOrderProve")
    public GyResult uploadOrderProve(Integer userId, Integer orderId, HttpServletRequest request) throws Exception {
        if (!ListUtil.judgeContain(Config.managerIdArray, userId)) return GyResult.fail("你没有权限上传已完成凭证");
        MultipartFile imgFile = ((MultipartRequest) request).getFile("imgFile");
        if (imgFile == null || imgFile.getSize() <= 0) return GyResult.fail("文件不能为空");
        else {
            String fileName = imgFile.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) return GyResult.fail("文件错误，无法上传");
            String saveName = new Date().getTime() + "_" + new Random().nextInt(9999) + fileName.substring(fileName.lastIndexOf("."));
            imgFile.transferTo(new File(Config.proveImgPath + saveName));
            String prove = Config.proveImgPath.replace("E:/IIS", Config.IISPath) + saveName;
            return orderService.uploadOrderProve(orderId, prove);
        }
    }

    /**
     * 完成订单
     */
    @RequestMapping("/completeOrder")
    public GyResult completeOrder(Integer userId, Integer orderId) throws Exception {
        if (!ListUtil.judgeContain(Config.managerIdArray, userId)) return GyResult.fail("你没有权限完成订单");
        return orderService.completeOrder(orderId);
    }

    /**
     * 关闭订单
     */
    @RequestMapping("/closeOrder")
    public GyResult closeOrder(Integer userId, Integer orderId) throws Exception {
        if (!ListUtil.judgeContain(Config.managerIdArray, userId)) return GyResult.fail("你没有权限关闭订单");
        return orderService.closeOrder(orderId);
    }

    /**
     * 导出订单
     */
    @RequestMapping("/exportOrder")
    public GyResult exportOrder(Integer userId) throws Exception {
        if (!ListUtil.judgeContain(Config.managerIdArray, userId)) return GyResult.fail("你没有权限导出订单");
        return orderService.exportOrder();
    }
}