package com.zhurui.bunnymall.home.bean;

import java.io.Serializable;

/**
 * Created by zhoux on 2017/8/16.
 */

public class CustomizedDetailBean implements Serializable{

    private long customserviceID;
    private long supplierID;
    private String  name;
    private String mainImage;
    private String info;
    private float price;
    private long userFootTypeDataID;
    private String suppliername;
    private int isfreepostage;
    private String townname;
    private String cityname;
    private int commentcount;
    private float postpage;



    public long getCustomserviceID() {
        return customserviceID;
    }

    public void setCustomserviceID(long customserviceID) {
        this.customserviceID = customserviceID;
    }

    public long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(long supplierID) {
        this.supplierID = supplierID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getUserFootTypeDataID() {
        return userFootTypeDataID;
    }

    public void setUserFootTypeDataID(long userFootTypeDataID) {
        this.userFootTypeDataID = userFootTypeDataID;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }


    public int getIsfreepostage() {
        return isfreepostage;
    }

    public void setIsfreepostage(int isfreepostage) {
        this.isfreepostage = isfreepostage;
    }

    public String getTownname() {
        return townname;
    }

    public void setTownname(String townname) {
        this.townname = townname;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public float getPostpage() {
        return postpage;
    }

    public void setPostpage(float postpage) {
        this.postpage = postpage;
    }
}
