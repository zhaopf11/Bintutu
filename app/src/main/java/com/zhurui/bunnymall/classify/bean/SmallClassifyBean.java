package com.zhurui.bunnymall.classify.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/18.
 */

public class SmallClassifyBean extends BaseBean {

    private long fashionID;
    private String name;
    private String sortIndex;
    private int validFlag;
    private String image;
    private long smallTypeID;
    private long bigTypeID;


    public long getFashionID() {
        return fashionID;
    }

    public void setFashionID(long fashionID) {
        this.fashionID = fashionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getSmallTypeID() {
        return smallTypeID;
    }

    public void setSmallTypeID(long smallTypeID) {
        this.smallTypeID = smallTypeID;
    }

    public long getBigTypeID() {
        return bigTypeID;
    }

    public void setBigTypeID(long bigTypeID) {
        this.bigTypeID = bigTypeID;
    }
}
