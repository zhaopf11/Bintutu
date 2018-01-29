package com.zhurui.bunnymall.home.bean;

import java.io.Serializable;

/**
 * Created by zhoux on 2017/8/15.
 */

public class RecommandProduct implements Serializable{

    private String mainSmallImage;
    private String price;
    private String name;
    private String normalPrice;
    private String mainImage;
    private String productID;
    private String propertiesInfo;
    private String info;
    private String flag;


    public String getMainSmallImage() {
        return mainSmallImage;
    }

    public void setMainSmallImage(String mainSmallImage) {
        this.mainSmallImage = mainSmallImage;
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

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getPropertiesInfo() {
        return propertiesInfo;
    }

    public void setPropertiesInfo(String propertiesInfo) {
        this.propertiesInfo = propertiesInfo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(String normalPrice) {
        this.normalPrice = normalPrice;
    }
}
