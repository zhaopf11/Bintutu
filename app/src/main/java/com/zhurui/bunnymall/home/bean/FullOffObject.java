package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/26.
 */

public class FullOffObject extends BaseBean {

    private List<BannerInfo> bannerList;
    private List<ThemeBean> themeList;
    private List<FullOffBean> fullcatList;
    private List<FullOffShowBean> showList;

    private List<FullOffBean> areaNavList;

    public List<BannerInfo> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerInfo> bannerList) {
        this.bannerList = bannerList;
    }

    public List<ThemeBean> getThemeList() {
        return themeList;
    }

    public void setThemeList(List<ThemeBean> themeList) {
        this.themeList = themeList;
    }

    public List<FullOffBean> getFullcatList() {
        return fullcatList;
    }

    public void setFullcatList(List<FullOffBean> fullcatList) {
        this.fullcatList = fullcatList;
    }

    public List<FullOffShowBean> getShowList() {
        return showList;
    }

    public void setShowList(List<FullOffShowBean> showList) {
        this.showList = showList;
    }

    public List<FullOffBean> getAreaNavList() {
        return areaNavList;
    }

    public void setAreaNavList(List<FullOffBean> areaNavList) {
        this.areaNavList = areaNavList;
    }
}
