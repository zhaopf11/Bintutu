package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/29.
 */

public class SupplierDetailBean extends BaseBean {
   private String name;
    private String logo;
    private String level;
    private String isFav;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }
}
