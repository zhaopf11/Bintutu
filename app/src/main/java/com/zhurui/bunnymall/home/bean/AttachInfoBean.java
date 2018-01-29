package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/11.
 */

public class AttachInfoBean extends BaseBean{


    private String stock;
    private String promotionActive;
    private String maxBuyNumber;
    private String endTime;
    private String groupBuyID;

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPromotionActive() {
        return promotionActive;
    }

    public void setPromotionActive(String promotionActive) {
        this.promotionActive = promotionActive;
    }

    public String getMaxBuyNumber() {
        return maxBuyNumber;
    }

    public void setMaxBuyNumber(String maxBuyNumber) {
        this.maxBuyNumber = maxBuyNumber;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGroupBuyID() {
        return groupBuyID;
    }

    public void setGroupBuyID(String groupBuyID) {
        this.groupBuyID = groupBuyID;
    }
}
