package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/26.
 */

public class ThemeBean extends BaseBean {

    private String promotionActiveTypeID;
    private String name;
    private String startTime;
    private String endTime;
    public void setPromotionActiveTypeID(String promotionActiveTypeID) {
        this.promotionActiveTypeID = promotionActiveTypeID;
    }
    public String getPromotionActiveTypeID() {
        return promotionActiveTypeID;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getEndTime() {
        return endTime;
    }
}
