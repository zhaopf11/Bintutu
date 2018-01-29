package com.zhurui.bunnymall.home.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by zhoux on 2017/8/15.
 */

public class NewProduct implements Serializable{
    private String mainSmallImage;
    private String price;
    private String name;
    private String normalPrice;
    private String mainImage;
    private String productID;
    private String flag;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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
