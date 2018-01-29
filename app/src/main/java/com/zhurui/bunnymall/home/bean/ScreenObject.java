package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/8/25.
 */

public class ScreenObject extends BaseBean {

    private List<PropertyGroupBean> propertieslist;

//    private Map<String,List<PropertyChildBean>> propertiesvaluelist;
    private List<PropertyChildBean> propertiesvaluelist;
    private List<PriceAreaBean> pricearealist;
    private List<CityBean> citylist;
    private List<VersionBean> versionlist;

    public List<PropertyGroupBean> getPropertieslist() {
        return propertieslist;
    }

    public void setPropertieslist(List<PropertyGroupBean> propertieslist) {
        this.propertieslist = propertieslist;
    }

    public List<PriceAreaBean> getPricearealist() {
        return pricearealist;
    }

    public void setPricearealist(List<PriceAreaBean> pricearealist) {
        this.pricearealist = pricearealist;
    }

    public List<CityBean> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<CityBean> citylist) {
        this.citylist = citylist;
    }

    public List<PropertyChildBean> getPropertiesvaluelist() {
        return propertiesvaluelist;
    }

    public void setPropertiesvaluelist(List<PropertyChildBean> propertiesvaluelist) {
        this.propertiesvaluelist = propertiesvaluelist;
    }

    public List<VersionBean> getVersionlist() {
        return versionlist;
    }

    public void setVersionlist(List<VersionBean> versionlist) {
        this.versionlist = versionlist;
    }
}
