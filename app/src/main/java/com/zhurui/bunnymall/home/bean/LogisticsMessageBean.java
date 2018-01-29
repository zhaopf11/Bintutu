package com.zhurui.bunnymall.home.bean;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/6 0006.
 */

public class LogisticsMessageBean {
    private String nu;
    private String message;
    private String ischeck;
    private String com;
    private String status;
    private String condition;
    private List<LogisticsDetailMessageBean> data;
    private String state;
    private String companyname;

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<LogisticsDetailMessageBean> getData() {
        return data;
    }

    public void setData(List<LogisticsDetailMessageBean> data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
