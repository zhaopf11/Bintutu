package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/5.
 */

public class CustomPropertyObject extends BaseBean {

    private List<CustomGroupProperty> custompropertieslist;
    private List<CustomeChildPropertyBean> cumstompropertiesvaluelist;


    public List<CustomGroupProperty> getCustompropertieslist() {
        return custompropertieslist;
    }

    public void setCustompropertieslist(List<CustomGroupProperty> custompropertieslist) {
        this.custompropertieslist = custompropertieslist;
    }

    public List<CustomeChildPropertyBean> getCumstompropertiesvaluelist() {
        return cumstompropertiesvaluelist;
    }

    public void setCumstompropertiesvaluelist(List<CustomeChildPropertyBean> cumstompropertiesvaluelist) {
        this.cumstompropertiesvaluelist = cumstompropertiesvaluelist;
    }
}
