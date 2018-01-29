package com.zhurui.bunnymall.home.bean;

/**
 * Created by zhaopf on 2017/9/6 0006.
 */

public class MessageBean {
    private String messageID;
    private String content;
    private String addTime;
    private String userID;
    private String typeID;
    private String trackingnumber;
    private String deliverytypeID;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getTrackingnumber() {
        return trackingnumber;
    }

    public void setTrackingnumber(String trackingnumber) {
        this.trackingnumber = trackingnumber;
    }

    public String getDeliverytypeID() {
        return deliverytypeID;
    }

    public void setDeliverytypeID(String deliverytypeID) {
        this.deliverytypeID = deliverytypeID;
    }
}
