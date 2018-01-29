package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/1.
 */

public class PointDetailBean extends BaseBean {
    private String c_userPointHistoryTypeName;
    private String userPointHistoryID;
    private String userID;
    private String relatedID;
    private String userPointHistoryTypeID;
    private String point;
    private String info;
    private String addTime;

    public void setC_userPointHistoryTypeName(String c_userPointHistoryTypeName) {
        this.c_userPointHistoryTypeName = c_userPointHistoryTypeName;
    }

    public String getC_userPointHistoryTypeName() {
        return c_userPointHistoryTypeName;
    }

    public void setUserPointHistoryID(String userPointHistoryID) {
        this.userPointHistoryID = userPointHistoryID;
    }

    public String getUserPointHistoryID() {
        return userPointHistoryID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setRelatedID(String relatedID) {
        this.relatedID = relatedID;
    }

    public String getRelatedID() {
        return relatedID;
    }

    public void setUserPointHistoryTypeID(String userPointHistoryTypeID) {
        this.userPointHistoryTypeID = userPointHistoryTypeID;
    }

    public String getUserPointHistoryTypeID() {
        return userPointHistoryTypeID;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPoint() {
        return point;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAddTime() {
        return addTime;
    }
}
