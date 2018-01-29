package com.zhurui.bunnymall.mine.bean;

import java.io.Serializable;

/**
 * Created by zhaopf on 2017/9/5 0005.
 */

public class CollectionsProductBean implements Serializable{

    private String favoriteProductID;
    private String userID;
    private String productID;
    private String addTime;
    private String groupBuyFlag;
    private String customFlag;
    private String name;
    private String mainImage;
    private String fashionID;
    private String price;
    private String normalPrice;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFavoriteProductID() {
        return favoriteProductID;
    }

    public void setFavoriteProductID(String favoriteProductID) {
        this.favoriteProductID = favoriteProductID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getGroupBuyFlag() {
        return groupBuyFlag;
    }

    public void setGroupBuyFlag(String groupBuyFlag) {
        this.groupBuyFlag = groupBuyFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomFlag() {
        return customFlag;
    }

    public void setCustomFlag(String customFlag) {
        this.customFlag = customFlag;
    }

    public String getFashionID() {
        return fashionID;
    }

    public void setFashionID(String fashionID) {
        this.fashionID = fashionID;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
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
