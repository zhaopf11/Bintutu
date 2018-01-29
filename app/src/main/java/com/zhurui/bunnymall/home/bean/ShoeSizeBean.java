package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/21.
 */

public class ShoeSizeBean extends BaseBean {
    private String name;
    private long shoeSizeID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getShoeSizeID() {
        return shoeSizeID;
    }

    public void setShoeSizeID(long shoeSizeID) {
        this.shoeSizeID = shoeSizeID;
    }
}
