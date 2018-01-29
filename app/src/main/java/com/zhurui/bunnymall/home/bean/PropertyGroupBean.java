package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/24.
 */

public class PropertyGroupBean extends BaseBean {

    private String customPropertiesID;
    private String name;
    private String alias;

    public String getCustomPropertiesID() {
        return customPropertiesID;
    }

    public void setCustomPropertiesID(String customPropertiesID) {
        this.customPropertiesID = customPropertiesID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


}
