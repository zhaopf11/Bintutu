package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/29.
 */

public class AddCartBean extends BaseBean {


    private String sizeid;
    private String colorid;
    private String number;
    private String maxstock;
    private String price;
    private String issuccess;
    private String productid;
    private String custompropertiesinfo;
    private String maxbuynumber;
    private String typeid;
    private String minbuynumber;
    private String supplierid;
    private String userfoottypedataid;

    public void setSizeid(String sizeid) {
        this.sizeid = sizeid;
    }

    public String getSizeid() {
        return sizeid;
    }

    public void setColorid(String colorid) {
        this.colorid = colorid;
    }

    public String getColorid() {
        return colorid;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setMaxstock(String maxstock) {
        this.maxstock = maxstock;
    }

    public String getMaxstock() {
        return maxstock;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setIssuccess(String issuccess) {
        this.issuccess = issuccess;
    }

    public String getIssuccess() {
        return issuccess;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductid() {
        return productid;
    }

    public void setCustompropertiesinfo(String custompropertiesinfo) {
        this.custompropertiesinfo = custompropertiesinfo;
    }

    public String getCustompropertiesinfo() {
        return custompropertiesinfo;
    }

    public void setMaxbuynumber(String maxbuynumber) {
        this.maxbuynumber = maxbuynumber;
    }

    public String getMaxbuynumber() {
        return maxbuynumber;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setMinbuynumber(String minbuynumber) {
        this.minbuynumber = minbuynumber;
    }

    public String getMinbuynumber() {
        return minbuynumber;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setUserfoottypedataid(String userfoottypedataid) {
        this.userfoottypedataid = userfoottypedataid;
    }

    public String getUserfoottypedataid() {
        return userfoottypedataid;
    }

}
