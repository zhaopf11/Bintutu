package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/20.
 */

public class TeamBuyBean extends BaseBean{

    private String endTime;
    private long endTimeMills;
    private String image;
    private float price;
    private long groupBuyID;
    private String maxBuyNumber;
    private String minBuyNumber;
    private String startTime;
    private long startTimeMills;
    private int stock;
    private float shoppingPrice;


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getEndTimeMills() {
        return endTimeMills;
    }

    public void setEndTimeMills(long endTimeMills) {
        this.endTimeMills = endTimeMills;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getGroupBuyID() {
        return groupBuyID;
    }

    public void setGroupBuyID(long groupBuyID) {
        this.groupBuyID = groupBuyID;
    }

    public String getMaxBuyNumber() {
        return maxBuyNumber;
    }

    public void setMaxBuyNumber(String maxBuyNumber) {
        this.maxBuyNumber = maxBuyNumber;
    }

    public String getMinBuyNumber() {
        return minBuyNumber;
    }

    public void setMinBuyNumber(String minBuyNumber) {
        this.minBuyNumber = minBuyNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getStartTimeMills() {
        return startTimeMills;
    }

    public void setStartTimeMills(long startTimeMills) {
        this.startTimeMills = startTimeMills;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getShoppingPrice() {
        return shoppingPrice;
    }

    public void setShoppingPrice(float shoppingPrice) {
        this.shoppingPrice = shoppingPrice;
    }
}
