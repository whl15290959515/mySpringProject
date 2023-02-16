package com.example.myspringdemo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 */
public class User implements Serializable {

    public static final long serialVersionUID = 1L;

    public Long id;


    //姓名
    public String name;


    //手机号
    public String phone;


    //性别 0 女 1 男
    public String sex;


    //身份证号
    public String idNumber;


    //头像
    public String avatar;


    //状态 0:禁用，1:正常
    public Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
