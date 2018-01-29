package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/22.
 */

public class PersonaInfoBean extends BaseBean {
    private String c_fpcount;
    private String c_shopcount;
    private String c_bscount;

    public String getC_fpcount() {
        return c_fpcount;
    }

    public void setC_fpcount(String c_fpcount) {
        this.c_fpcount = c_fpcount;
    }

    public String getC_shopcount() {
        return c_shopcount;
    }

    public void setC_shopcount(String c_shopcount) {
        this.c_shopcount = c_shopcount;
    }

    public String getC_bscount() {
        return c_bscount;
    }

    public void setC_bscount(String c_bscount) {
        this.c_bscount = c_bscount;
    }
}
