package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhaopf on 2017/9/2 0002.
 */

public class UserFootDataDetailBean extends BaseBean {
    private String userFootTypeDataID;
    private String measureCode;
    private String name;
    private String sex;
    private String stature;
    private String age;
    private String shoeSize;
    private String c_footArchName;
    private String c_footVarusValgusName;
    private String weight;
    private String footDesc;
    private String birthday;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFootDesc() {
        return footDesc;
    }

    public void setFootDesc(String footDesc) {
        this.footDesc = footDesc;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUserFootTypeDataID() {
        return userFootTypeDataID;
    }

    public void setUserFootTypeDataID(String userFootTypeDataID) {
        this.userFootTypeDataID = userFootTypeDataID;
    }

    public String getMeasureCode() {
        return measureCode;
    }

    public void setMeasureCode(String measureCode) {
        this.measureCode = measureCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStature() {
        return stature;
    }

    public void setStature(String stature) {
        this.stature = stature;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getShoeSize() {
        return shoeSize;
    }

    public void setShoeSize(String shoeSize) {
        this.shoeSize = shoeSize;
    }

    public String getC_footArchName() {
        return c_footArchName;
    }

    public void setC_footArchName(String c_footArchName) {
        this.c_footArchName = c_footArchName;
    }

    public String getC_footVarusValgusName() {
        return c_footVarusValgusName;
    }

    public void setC_footVarusValgusName(String c_footVarusValgusName) {
        this.c_footVarusValgusName = c_footVarusValgusName;
    }
}
