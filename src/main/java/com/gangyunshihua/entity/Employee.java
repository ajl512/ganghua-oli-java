package com.gangyunshihua.entity;

import javax.persistence.*;

/**
 * 员工表
 */
@Entity
@Table(name = "gy_employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer user_id;
    private Integer status = 1;
    private Integer driver_status;
    private Integer supercargo_status;
    private String name;
    private String mobile;
    private String id_number;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDriver_status() {
        return driver_status;
    }

    public void setDriver_status(Integer driver_status) {
        this.driver_status = driver_status;
    }

    public Integer getSupercargo_status() {
        return supercargo_status;
    }

    public void setSupercargo_status(Integer supercargo_status) {
        this.supercargo_status = supercargo_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }
}
