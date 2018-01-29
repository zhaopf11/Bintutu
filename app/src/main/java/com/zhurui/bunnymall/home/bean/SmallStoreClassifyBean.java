package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/14.
 */

public class SmallStoreClassifyBean extends BaseBean {

    private String smallTypeID;
    private String name;
    private String fashionID;


    public String getSmallTypeID() {
        return smallTypeID;
    }

    public void setSmallTypeID(String smallTypeID) {
        this.smallTypeID = smallTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFashionID() {
        return fashionID;
    }

    public void setFashionID(String fashionID) {
        this.fashionID = fashionID;
    }
}
