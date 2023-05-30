package com.gangyunshihua.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单表
 */
@Entity
@Table(name = "gy_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer user_id;
    private Integer driver_id = 0;
    private Integer supercargo_id = 0;
    private Integer status = 1;//状态 1进行中/2已完成/3已关闭
    private Integer pick_mode;//取货方式 1自提/2送货上门
    private String number;
    private String mobile;
    private String company;
    private String car_number = "";
    private Float car_load = 0f;
    private String prove = "";
    private Date create_time = new Date();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(Integer driver_id) {
        this.driver_id = driver_id;
    }

    public Integer getSupercargo_id() {
        return supercargo_id;
    }

    public void setSupercargo_id(Integer supercargo_id) {
        this.supercargo_id = supercargo_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPick_mode() {
        return pick_mode;
    }

    public void setPick_mode(Integer pick_mode) {
        this.pick_mode = pick_mode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public Float getCar_load() {
        return car_load;
    }

    public void setCar_load(Float car_load) {
        this.car_load = car_load;
    }

    public String getProve() {
        return prove;
    }

    public void setProve(String prove) {
        this.prove = prove;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
