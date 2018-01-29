package com.zhurui.bunnymall.home.bean;

import java.io.Serializable;

/**
 * Created by zhoux on 2017/8/16.
 */

public class PersonalCustomizedbean implements Serializable{

    private int productID;
    private float price;
    private String name;
    private String mainImage;
    private String info;
    private String normalPrice;


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(String normalPrice) {
        this.normalPrice = normalPrice;
    }
}
