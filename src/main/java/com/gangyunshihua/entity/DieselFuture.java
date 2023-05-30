package com.gangyunshihua.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 预设柴油表
 */
@Entity
@Table(name = "gy_diesel_future")
public class DieselFuture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;
    private Float price;
    private Date valid_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getValid_time() {
        return valid_time;
    }

    public void setValid_time(Date valid_time) {
        this.valid_time = valid_time;
    }
}
