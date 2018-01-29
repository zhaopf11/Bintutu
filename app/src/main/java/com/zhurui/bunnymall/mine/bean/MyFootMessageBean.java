package com.zhurui.bunnymall.mine.bean;

import java.io.Serializable;

/**
 * Created by zhaopf on 2017/9/2 0002.
 */

public class MyFootMessageBean implements Serializable{
    private String name;
    private String measureCode;
    private String sex;
    private String shoeSize;
    private String userFootTypeDataID;
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserFootTypeDataID() {
        return userFootTypeDataID;
    }

    public void setUserFootTypeDataID(String userFootTypeDataID) {
        this.userFootTypeDataID = userFootTypeDataID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasureCode() {
        return measureCode;
    }

    public void setMeasureCode(String measureCode) {
        this.measureCode = measureCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getShoeSize() {
        return shoeSize;
    }

    public void setShoeSize(String shoeSize) {
        this.shoeSize = shoeSize;
    }
}
