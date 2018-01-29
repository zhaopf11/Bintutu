package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/24.
 */

public class PropertyObj extends BaseBean {
    private List<PropertyGroupBean> custompropertieslist;

    private List<PropertyChildBean> cumstompropertiesvaluelist;

    public List<PropertyGroupBean> getCustompropertieslist() {
        return custompropertieslist;
    }

    public void setCustompropertieslist(List<PropertyGroupBean> custompropertieslist) {
        this.custompropertieslist = custompropertieslist;
    }

    public List<PropertyChildBean> getCumstompropertiesvaluelist() {
        return cumstompropertiesvaluelist;
    }

    public void setCumstompropertiesvaluelist(List<PropertyChildBean> cumstompropertiesvaluelist) {
        this.cumstompropertiesvaluelist = cumstompropertiesvaluelist;
    }
}
