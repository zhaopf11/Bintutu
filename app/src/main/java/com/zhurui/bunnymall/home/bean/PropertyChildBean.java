package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/24.
 */

public class PropertyChildBean extends BaseBean{

    private String customPropertiesValueID;
    private String name;
    private String customPropertiesID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomPropertiesValueID() {
        return customPropertiesValueID;
    }

    public void setCustomPropertiesValueID(String customPropertiesValueID) {
        this.customPropertiesValueID = customPropertiesValueID;
    }

    public String getCustomPropertiesID() {
        return customPropertiesID;
    }

    public void setCustomPropertiesID(String customPropertiesID) {
        this.customPropertiesID = customPropertiesID;
    }
}
