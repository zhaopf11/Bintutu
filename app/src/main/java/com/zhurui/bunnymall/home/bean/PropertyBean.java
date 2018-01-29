package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/21.
 */

public class PropertyBean extends BaseBean {
    private String sortIndex;
    private int validFlag;
    private String propTypeID;
    private String name;
    private String img;
    private String footprint;
    private long customPropertiesID;


    public String getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(String sortIndex) {
        this.sortIndex = sortIndex;
    }

    public int getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(int validFlag) {
        this.validFlag = validFlag;
    }

    public String getPropTypeID() {
        return propTypeID;
    }

    public void setPropTypeID(String propTypeID) {
        this.propTypeID = propTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFootprint() {
        return footprint;
    }

    public void setFootprint(String footprint) {
        this.footprint = footprint;
    }

    public long getCustomPropertiesID() {
        return customPropertiesID;
    }

    public void setCustomPropertiesID(long customPropertiesID) {
        this.customPropertiesID = customPropertiesID;
    }
}
