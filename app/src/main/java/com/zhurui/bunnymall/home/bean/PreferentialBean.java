package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/5.
 */

public class PreferentialBean extends BaseBean {

    private String name;
    private String productID;
    private String endTime;
    private String endTimeMills;
    private String image;
    private String price;
    private String groupBuyID;
    private String maxBuyNumber;
    private String minBuyNumber;
    private String startTime;
    private String startTimeMills;
    private String stock;
    private String shoppingPrice;

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductID() {
        return productID;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTimeMills(String endTimeMills) {
        this.endTimeMills = endTimeMills;
    }

    public String getEndTimeMills() {
        return endTimeMills;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setGroupBuyID(String groupBuyID) {
        this.groupBuyID = groupBuyID;
    }

    public String getGroupBuyID() {
        return groupBuyID;
    }

    public void setMaxBuyNumber(String maxBuyNumber) {
        this.maxBuyNumber = maxBuyNumber;
    }

    public String getMaxBuyNumber() {
        return maxBuyNumber;
    }

    public void setMinBuyNumber(String minBuyNumber) {
        this.minBuyNumber = minBuyNumber;
    }

    public String getMinBuyNumber() {
        return minBuyNumber;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTimeMills(String startTimeMills) {
        this.startTimeMills = startTimeMills;
    }

    public String getStartTimeMills() {
        return startTimeMills;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStock() {
        return stock;
    }

    public void setShoppingPrice(String shoppingPrice) {
        this.shoppingPrice = shoppingPrice;
    }

    public String getShoppingPrice() {
        return shoppingPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
