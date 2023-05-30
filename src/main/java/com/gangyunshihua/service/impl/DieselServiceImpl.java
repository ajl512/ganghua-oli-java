package com.gangyunshihua.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gangyunshihua.entity.Diesel;
import com.gangyunshihua.entity.DieselFuture;
import com.gangyunshihua.entity.DieselLog;
import com.gangyunshihua.pojo.Config;
import com.gangyunshihua.pojo.GyResult;
import com.gangyunshihua.repository.DieselFutureRepository;
import com.gangyunshihua.repository.DieselLogRepository;
import com.gangyunshihua.repository.DieselRepository;
import com.gangyunshihua.service.DieselService;
import com.gangyunshihua.utils.ListUtil;
import com.gangyunshihua.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DieselServiceImpl implements DieselService {

    @Autowired
    private DieselRepository dieselRepository;

    @Autowired
    private DieselFutureRepository dieselFutureRepository;

    @Autowired
    private DieselLogRepository dieselLogRepository;

    @Override
    public GyResult findDiesel(Integer userId) throws Exception {
        JSONObject data = new JSONObject();
        //显示的柴油价格
        Date yesterday = DateUtils.addDays(TimeUtil.getTodayZeroDate(), -1);
        JSONArray showDieselArray = new JSONArray();
        for (Object[] objects : dieselRepository.findDiesel(yesterday)) {
            JSONObject object = new JSONObject();
            object.put("dieselId", objects[0]);
            object.put("type", objects[1]);
            object.put("price", objects[2]);
            object.put("differencePrice", objects[3]);
            showDieselArray.add(object);
        }
        data.put("showDieselArray", showDieselArray);
        //未显示的柴油价格
        if (ListUtil.judgeContain(Config.managerIdArray, userId)) {
            JSONArray hideDieselArray = new JSONArray();
            for (Diesel diesel : dieselRepository.findByStatus(2)) {
                JSONObject object = new JSONObject();
                object.put("type", diesel.getType());
                object.put("price", diesel.getPrice());
                hideDieselArray.add(object);
            }
            data.put("hideDieselArray", hideDieselArray);
        }
        return GyResult.success(data);
    }

    @Override
    public GyResult findDieselLog(Integer dieselId) throws Exception {
        Diesel diesel = dieselRepository.findOne(dieselId);
        if (diesel == null) return GyResult.fail("该柴油不存在");
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        JSONArray data = new JSONArray();
        for (DieselLog dieselLog : dieselLogRepository.findDieselLog(diesel.getType(), DateUtils.addDays(new Date(), -8))) {
            JSONObject object = new JSONObject();
            object.put("price", dieselLog.getPrice());
            object.put("date", sdf.format(dieselLog.getValid_time()));
            data.add(object);
        }
        return GyResult.success(data);
    }

    @Override
    public GyResult findDieselFuture() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        JSONArray data = new JSONArray();
        for (DieselFuture dieselFuture : dieselFutureRepository.findAll()) {
            JSONObject object = new JSONObject();
            object.put("type", dieselFuture.getType());
            object.put("price", dieselFuture.getPrice());
            object.put("validTime", sdf.format(dieselFuture.getValid_time()));
            data.add(object);
        }
        return GyResult.success(data);
    }

    @Override
    public GyResult editDiesel(String dieselJson) throws Exception {
        JSONArray dieselArray = JSONArray.parseArray(dieselJson);
        for (int i = 0; i < dieselArray.size(); i++) {
            JSONObject dieselObject = dieselArray.getJSONObject(i);
            String type = dieselObject.getString("type");
            if (StringUtils.isEmpty(type)) return GyResult.fail("柴油类型不能为空");
            Float price = dieselObject.getFloat("price");
            if (price <= 0) return GyResult.fail("柴油价格必须大于0");
        }
        //删除之前预设的柴油价格
        dieselFutureRepository.deleteAll();
        //隐藏所有柴油价格
        dieselRepository.hideAll();
        //设置柴油价格
        for (int i = 0; i < dieselArray.size(); i++) {
            JSONObject dieselObject = dieselArray.getJSONObject(i);
            String type = dieselObject.getString("type");
            Float price = dieselObject.getFloat("price");
            Diesel diesel = dieselRepository.findByType(type);
            if (diesel == null) {
                diesel = new Diesel();
                diesel.setType(type);
            }
            diesel.setPrice(price);
            diesel.setStatus(1);
            dieselRepository.save(diesel);
        }
        return GyResult.success();
    }

    @Override
    public GyResult editDieselFuture(String dieselJson, String validTime) throws Exception {
        Date valid = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(validTime);
        if (valid.getTime() < new Date().getTime()) return GyResult.fail("生效时间不得小于当前时间");
        JSONArray dieselArray = JSONArray.parseArray(dieselJson);
        for (int i = 0; i < dieselArray.size(); i++) {
            JSONObject dieselObject = dieselArray.getJSONObject(i);
            String type = dieselObject.getString("type");
            if (StringUtils.isEmpty(type)) return GyResult.fail("柴油类型不能为空");
            Float price = dieselObject.getFloat("price");
            if (price <= 0) return GyResult.fail("柴油价格必须大于0");
        }
        //删除之前预设的柴油价格
        dieselFutureRepository.deleteAll();
        //预设柴油价格
        for (int i = 0; i < dieselArray.size(); i++) {
            JSONObject dieselObject = dieselArray.getJSONObject(i);
            String type = dieselObject.getString("type");
            Float price = dieselObject.getFloat("price");
            DieselFuture dieselFuture = new DieselFuture();
            dieselFuture.setType(type);
            dieselFuture.setPrice(price);
            dieselFuture.setValid_time(valid);
            dieselFutureRepository.save(dieselFuture);
        }
        return GyResult.success();
    }

    @Override
    public void addDieselLog() throws Exception {
        Date validTime = TimeUtil.getTodayZeroDate();
        for (Diesel diesel : dieselRepository.findByStatus(1)) {
            DieselLog dieselLog = new DieselLog();
            dieselLog.setType(diesel.getType());
            dieselLog.setPrice(diesel.getPrice());
            dieselLog.setValid_time(validTime);
            dieselLogRepository.save(dieselLog);
        }
    }

    @Override
    public void flushValid() throws Exception {
        List<DieselFuture> dieselFutures = dieselFutureRepository.findValid(new Date());
        if (dieselFutures.size() > 0) {
            //隐藏所有柴油价格
            dieselRepository.hideAll();
            //设置柴油价格
            for (DieselFuture dieselFuture : dieselFutures) {
                Diesel diesel = dieselRepository.findByType(dieselFuture.getType());
                if (diesel == null) {
                    diesel = new Diesel();
                    diesel.setType(dieselFuture.getType());
                }
                diesel.setPrice(dieselFuture.getPrice());
                diesel.setStatus(1);
                dieselRepository.save(diesel);
            }
            //预设的柴油价格
            dieselFutureRepository.deleteAll();
        }
    }
}