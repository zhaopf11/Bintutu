package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/25.
 */

public class CityBean extends BaseBean {

    private String provinceID;
    private String validFlag;
    private String name;
    private String cityID;
    private String provinceName;

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return provinceName;
    }

}
