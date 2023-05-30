package com.gangyunshihua.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gangyunshihua.entity.*;
import com.gangyunshihua.pojo.Config;
import com.gangyunshihua.pojo.GyResult;
import com.gangyunshihua.repository.*;
import com.gangyunshihua.service.OrderService;
import com.gangyunshihua.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private DieselRepository dieselRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public GyResult addOrder(Integer userId, String dieselJson, String mobile, String company, String carNumber, Float carLoad, Integer driverId, Integer supercargoId) throws Exception {
        JSONArray dieselArray = JSONArray.parseArray(dieselJson);
        if (dieselArray.size() == 0) return GyResult.fail("至少购买一种油品");
        List<Diesel> diesels = new ArrayList<Diesel>();
        for (int i = 0; i < dieselArray.size(); i++) {
            JSONObject dieselObject = dieselArray.getJSONObject(i);
            Float weight = dieselObject.getFloat("weight");
            if (weight <= 0) return GyResult.fail("油品重量必须大于0");
            Integer dieselId = dieselObject.getInteger("dieselId");
            Diesel diesel = dieselRepository.findOne(dieselId);
            if (diesel == null) return GyResult.fail("包含不存在的油品");
            if (diesel.getStatus() == 2) return GyResult.fail("包含未展示的油品");
            diesels.add(diesel);
        }
        Employee driver = employeeRepository.findOne(driverId);
        if (driver == null || driver.getStatus() == 2) return GyResult.fail("该驾驶员不存在");
//        if (driver.getDriver_status() != 1) return GyResult.fail("该员工不是驾驶员");
        if (driver.getUser_id() != userId.intValue()) return GyResult.fail("该驾驶员不是你的员工");
        Employee supercargo = employeeRepository.findOne(supercargoId);
        if (supercargo == null || supercargo.getStatus() == 2) return GyResult.fail("该押运员不存在");
//        if (supercargo.getSupercargo_status() != 1) return GyResult.fail("该员工不是押运员");
        if (supercargo.getUser_id() != userId.intValue()) return GyResult.fail("该押运员不是你的员工");
        //修改用户信息
        User user = userRepository.findOne(userId);
        if (StringUtils.isEmpty(user.getCompany())) user.setCompany(company);
        if (StringUtils.isEmpty(user.getMobile())) user.setMobile(mobile);
        userRepository.save(user);
        //新增订单
        Order order = new Order();
        order.setUser_id(userId);
        order.setDriver_id(driverId);
        order.setSupercargo_id(supercargoId);
        order.setPick_mode(1);
        order.setNumber(getOrderNumber(userId));
        order.setMobile(mobile);
        order.setCompany(company);
        order.setCar_number(carNumber);
        order.setCar_load(carLoad);
        orderRepository.save(order);
        //新增订单项
        for (int i = 0; i < dieselArray.size(); i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder_id(order.getId());
            orderItem.setType(diesels.get(i).getType());
            orderItem.setPrice(diesels.get(i).getPrice());
            orderItem.setWeight(dieselArray.getJSONObject(i).getFloat("weight"));
            orderItemRepository.save(orderItem);
        }
        //发短信
        HttpClientUtil.sendMessage();
        return GyResult.success();
    }

    @Override
    public GyResult addOrder(Integer userId, String dieselJson, String mobile, String company) throws Exception {
        JSONArray dieselArray = JSONArray.parseArray(dieselJson);
        if (dieselArray.size() == 0) return GyResult.fail("至少购买一种油品");
        List<Diesel> diesels = new ArrayList<Diesel>();
        for (int i = 0; i < dieselArray.size(); i++) {
            JSONObject dieselObject = dieselArray.getJSONObject(i);
            Float weight = dieselObject.getFloat("weight");
            if (weight <= 0) return GyResult.fail("油品重量必须大于0");
            Integer dieselId = dieselObject.getInteger("dieselId");
            Diesel diesel = dieselRepository.findOne(dieselId);
            if (diesel == null) return GyResult.fail("包含不存在的油品");
            if (diesel.getStatus() == 2) return GyResult.fail("包含未展示的油品");
            diesels.add(diesel);
        }
        //修改用户信息
        User user = userRepository.findOne(userId);
        if (StringUtils.isEmpty(user.getCompany())) user.setCompany(company);
        if (StringUtils.isEmpty(user.getMobile())) user.setMobile(mobile);
        userRepository.save(user);
        //新增订单
        Order order = new Order();
        order.setUser_id(userId);
        order.setPick_mode(2);
        order.setNumber(getOrderNumber(userId));
        order.setMobile(mobile);
        order.setCompany(company);
        orderRepository.save(order);
        //新增订单项
        for (int i = 0; i < dieselArray.size(); i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder_id(order.getId());
            orderItem.setType(diesels.get(i).getType());
            orderItem.setPrice(diesels.get(i).getPrice());
            orderItem.setWeight(dieselArray.getJSONObject(i).getFloat("weight"));
            orderItemRepository.save(orderItem);
        }
        //发短信
        HttpClientUtil.sendMessage();
        return GyResult.success();
    }

    @Override
    public GyResult findMyOrder(Integer userId, Integer[] orderStatusArray, Integer page) throws Exception {
        JSONArray orderArray = new JSONArray();
        for (Object[] objects : orderRepository.findMyOrder(userId, orderStatusArray, page * 20, 20)) {
            JSONObject object = new JSONObject();
            object.put("orderId", objects[0]);
            object.put("orderNumber", objects[1]);
            object.put("dieselInfo", objects[2]);
            object.put("driverName", objects[3]);
            object.put("supercargoName", objects[4]);
            object.put("createTime", objects[5]);
            object.put("orderStatus", objects[6]);
            object.put("pickMode", objects[7]);
            object.put("mobile", objects[8]);
            object.put("company", objects[9]);
            object.put("carNumber", objects[10]);
            object.put("carLoad", objects[11]);
            object.put("prove", objects[12]);
            object.put("driverIdNumber", objects[13]);
            object.put("driverMobile", objects[14]);
            object.put("supercargoIdNumber", objects[15]);
            object.put("supercargoMobile", objects[16]);
            orderArray.add(object);
        }
        JSONObject data = new JSONObject();
        data.put("orderArray", orderArray);
        data.put("totalCount", orderRepository.findMyOrderCount(userId, orderStatusArray));
        return GyResult.success(data);
    }

    @Override
    public GyResult findMyLastOrder(Integer userId) throws Exception {
        Integer orderId = orderRepository.findMyLastOrderId(userId);
        JSONObject data = new JSONObject();
        if (orderId == null) {
            data.put("orderId", 0);
        } else {
            Order order = orderRepository.findOne(orderId);
            Employee driver = employeeRepository.findOne(order.getDriver_id());
            Employee supercargo = employeeRepository.findOne(order.getSupercargo_id());
            data.put("orderId", order.getId());
            data.put("pickMode", order.getPick_mode());
            data.put("mobile", order.getMobile());
            data.put("company", order.getCompany());
            data.put("carNumber", order.getCar_number());
            data.put("carLoad", order.getCar_load());
//            if (driver.getStatus() == 1 && driver.getDriver_status() == 1) {
            if (driver != null && driver.getStatus() == 1) {
                JSONObject driverObject = new JSONObject();
                driverObject.put("employeeId", order.getDriver_id());
                driverObject.put("name", driver.getName());
                driverObject.put("idNumber", driver.getId_number());
                driverObject.put("mobile", driver.getMobile());
                data.put("driverObject", driverObject);
            }
//            if (supercargo.getStatus() == 1 && supercargo.getSupercargo_status() == 1) {
            if (supercargo != null && supercargo.getStatus() == 1) {
                JSONObject supercargoObject = new JSONObject();
                supercargoObject.put("employeeId", order.getSupercargo_id());
                supercargoObject.put("name", supercargo.getName());
                supercargoObject.put("idNumber", supercargo.getId_number());
                supercargoObject.put("mobile", supercargo.getMobile());
                data.put("supercargoObject", supercargoObject);
            }
        }
        return GyResult.success(data);
    }

    @Override
    public GyResult findOrder(Integer[] orderStatusArray, Integer page) throws Exception {
        JSONArray orderArray = new JSONArray();
        for (Object[] objects : orderRepository.findOrder(orderStatusArray, page * 20, 20)) {
            JSONObject object = new JSONObject();
            object.put("orderId", objects[0]);
            object.put("orderNumber", objects[1]);
            object.put("dieselInfo", objects[2]);
            object.put("driverName", objects[3]);
            object.put("supercargoName", objects[4]);
            object.put("createTime", objects[5]);
            object.put("orderStatus", objects[6]);
            object.put("pickMode", objects[7]);
            object.put("mobile", objects[8]);
            object.put("company", objects[9]);
            object.put("carNumber", objects[10]);
            object.put("carLoad", objects[11]);
            object.put("prove", objects[12]);
            object.put("driverIdNumber", objects[13]);
            object.put("driverMobile", objects[14]);
            object.put("supercargoIdNumber", objects[15]);
            object.put("supercargoMobile", objects[16]);
            orderArray.add(object);
        }
        JSONObject data = new JSONObject();
        data.put("orderArray", orderArray);
        data.put("totalCount", orderRepository.findOrderCount(orderStatusArray));
        return GyResult.success(data);
    }

    @Override
    public GyResult uploadOrderProve(Integer orderId, String prove) throws Exception {
        Order order = orderRepository.findOne(orderId);
        if (order == null) return GyResult.fail("该订单不存在");
        if (order.getStatus() != 1) return GyResult.fail("订单状态错误");
        order.setStatus(2);
        order.setProve(prove);
        orderRepository.save(order);
        return GyResult.success();
    }

    @Override
    public GyResult completeOrder(Integer orderId) throws Exception {
        Order order = orderRepository.findOne(orderId);
        if (order == null) return GyResult.fail("该订单不存在");
        if (order.getStatus() != 1) return GyResult.fail("订单状态错误");
        order.setStatus(2);
        orderRepository.save(order);
        return GyResult.success();
    }

    @Override
    public GyResult closeOrder(Integer orderId) throws Exception {
        Order order = orderRepository.findOne(orderId);
        if (order == null) return GyResult.fail("该订单不存在");
        order.setStatus(3);
        orderRepository.save(order);
        return GyResult.success();
    }

    @Override
    public GyResult exportOrder() throws Exception {
        //生成订单数据表格
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("订单数据");
        //创建首行单元格样式
        XSSFCellStyle firstCellStyle = workbook.createCellStyle();
        firstCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
        firstCellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        firstCellStyle.setBorderBottom(BorderStyle.THIN);//下边框
        firstCellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        firstCellStyle.setBorderTop(BorderStyle.THIN);//上边框
        firstCellStyle.setBorderRight(BorderStyle.THIN);//右边框
        XSSFFont font = workbook.createFont();
        font.setBold(true); //字体加粗
        firstCellStyle.setFont(font);
        //创建首行
        String[] firstRowArray = new String[]{"订单编号", "下单时间", "订单状态", "取货方式", "油品信息", "联系电话", "公司名称", "车牌号", "车辆核载", "司机姓名", "司机身份证号", "司机联系电话", "押运员姓名", "押运员身份证号", "押运员联系电话"};
        XSSFRow firstRow = sheet.createRow(0);
        firstRow.setHeight((short) 360);//行高
        for (int i = 0; i < firstRowArray.length; i++) {
            XSSFCell cell = firstRow.createCell(i);
            cell.setCellStyle(firstCellStyle);
            cell.setCellValue(firstRowArray[i]);
        }
        //创建数据行单元格样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setBorderBottom(BorderStyle.THIN);//下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        //创建数据行
        List<Object[]> objects = orderRepository.findOrder(new Integer[]{1, 2}, 0, 10000);
        for (Object[] objs : objects) {
            XSSFRow row = sheet.createRow(objects.indexOf(objs) + 1);
            row.setHeight((short) 360);//行高
            //订单编号
            XSSFCell cell0 = row.createCell(0);
            cell0.setCellStyle(cellStyle);
            cell0.setCellValue(objs[1].toString());
            //下单时间
            XSSFCell cell1 = row.createCell(1);
            cell1.setCellStyle(cellStyle);
            cell1.setCellValue(objs[5].toString());
            //订单状态
            XSSFCell cell2 = row.createCell(2);
            cell2.setCellStyle(cellStyle);
            cell2.setCellValue(Integer.parseInt(objs[6].toString()) == 1 ? "进行中" : "已完成");
            //取货方式
            XSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(cellStyle);
            cell3.setCellValue(Integer.parseInt(objs[7].toString()) == 1 ? "自提" : "送货上门");
            //油品信息
            XSSFCell cell4 = row.createCell(4);
            cell4.setCellStyle(cellStyle);
            cell4.setCellValue(objs[2].toString());
            //联系电话
            XSSFCell cell5 = row.createCell(5);
            cell5.setCellStyle(cellStyle);
            cell5.setCellValue(objs[8].toString());
            //公司名称
            XSSFCell cell6 = row.createCell(6);
            cell6.setCellStyle(cellStyle);
            cell6.setCellValue(objs[9].toString());
            //车牌号
            XSSFCell cell7 = row.createCell(7);
            cell7.setCellStyle(cellStyle);
            cell7.setCellValue(objs[10].toString());
            //车辆核载
            XSSFCell cell8 = row.createCell(8);
            cell8.setCellStyle(cellStyle);
            cell8.setCellValue(objs[11].toString());
            //司机姓名
            XSSFCell cell9 = row.createCell(9);
            cell9.setCellStyle(cellStyle);
            cell9.setCellValue(objs[3].toString());
            //司机身份证号
            XSSFCell cell10 = row.createCell(10);
            cell10.setCellStyle(cellStyle);
            cell10.setCellValue(objs[13].toString());
            //司机联系电话
            XSSFCell cell11 = row.createCell(11);
            cell11.setCellStyle(cellStyle);
            cell11.setCellValue(objs[14].toString());
            //押运员姓名
            XSSFCell cell12 = row.createCell(12);
            cell12.setCellStyle(cellStyle);
            cell12.setCellValue(objs[4].toString());
            //押运员身份证号
            XSSFCell cell13 = row.createCell(13);
            cell13.setCellStyle(cellStyle);
            cell13.setCellValue(objs[15].toString());
            //押运员联系电话
            XSSFCell cell14 = row.createCell(14);
            cell14.setCellStyle(cellStyle);
            cell14.setCellValue(objs[16].toString());
        }
        //根据表格内容调整列宽
        for (int i = 0; i < firstRowArray.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 2);
        }
        //生成Excel
        String saveName = new SimpleDateFormat("yyyyMMddHH").format(new Date()) + ".xlsx";
        workbook.write(new FileOutputStream(Config.orderExcelPath + saveName));
        //返回表格路径
        JSONObject data = new JSONObject();
        data.put("filePath", Config.orderExcelPath.replace("E:/IIS", "https://www.gangyunshihua.com:8012") + saveName);
        return GyResult.success(data);
    }

    private String getOrderNumber(Integer userId) throws Exception {
        String orderNumber = "";
        //港云石化 业务码 616
        orderNumber += "616";
        //年月日
        orderNumber += new SimpleDateFormat("yyyyMMdd").format(new Date());
        //时间戳后5位
        orderNumber += String.valueOf(new Date().getTime()).substring(8);
        //用户标识
        String identity = String.valueOf((3549L * userId + 819) * 354 % 10000);
        if (identity.length() == 1) orderNumber += "000" + identity;
        else if (identity.length() == 2) orderNumber += "00" + identity;
        else if (identity.length() == 3) orderNumber += "0" + identity;
        else orderNumber += identity;
        return orderNumber;
    }
}