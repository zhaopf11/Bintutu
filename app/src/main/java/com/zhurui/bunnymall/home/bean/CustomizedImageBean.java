package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/20.
 */

public class CustomizedImageBean extends BaseBean {

    private long customImageID;
    private long customServiceID;
    private String addTime;
    private String image;
    private String mainFlag;
    private String sortIndex;
    public long getCustomImageID() {
        return customImageID;
    }

    public void setCustomImageID(long customImageID) {
        this.customImageID = customImageID;
    }

    public long getCustomServiceID() {
        return customServiceID;
    }

    public void setCustomServiceID(long customServiceID) {
        this.customServiceID = customServiceID;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMainFlag() {
        return mainFlag;
    }

    public void setMainFlag(String mainFlag) {
        this.mainFlag = mainFlag;
    }

    public String getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(String sortIndex) {
        this.sortIndex = sortIndex;
    }
}
