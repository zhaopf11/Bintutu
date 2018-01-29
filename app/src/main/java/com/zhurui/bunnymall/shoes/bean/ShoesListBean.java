package com.zhurui.bunnymall.shoes.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhaopf on 2017/12/15.
 */

public class ShoesListBean extends BaseBean{
    private String infoID;
    private String infoTypeID;
    private String title;
    private String subTitle;
    private String image;
    private String tjFlag;
    private String viewCount;
    private String addTime;
    private String sortIndex;
    private String validFlag;
    private String sendWechatFlag;
    private String sendWechatTime;
    private String url;
    private String shareUrl;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfoID() {
        return infoID;
    }

    public void setInfoID(String infoID) {
        this.infoID = infoID;
    }

    public String getInfoTypeID() {
        return infoTypeID;
    }

    public void setInfoTypeID(String infoTypeID) {
        this.infoTypeID = infoTypeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTjFlag() {
        return tjFlag;
    }

    public void setTjFlag(String tjFlag) {
        this.tjFlag = tjFlag;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(String sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getSendWechatFlag() {
        return sendWechatFlag;
    }

    public void setSendWechatFlag(String sendWechatFlag) {
        this.sendWechatFlag = sendWechatFlag;
    }

    public String getSendWechatTime() {
        return sendWechatTime;
    }

    public void setSendWechatTime(String sendWechatTime) {
        this.sendWechatTime = sendWechatTime;
    }
}
