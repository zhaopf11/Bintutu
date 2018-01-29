package com.zhurui.bunnymall.mine.bean;

/**
 * Created by zhaopf on 2017/9/8 0008.
 */

public class CancleReasonBaen {
    private String reansonStr;
    private Boolean isSelect;
    private String reansonId;

    public String getReansonId() {
        return reansonId;
    }

    public void setReansonId(String reansonId) {
        this.reansonId = reansonId;
    }

    public String getReansonStr() {
        return reansonStr;
    }

    public void setReansonStr(String reansonStr) {
        this.reansonStr = reansonStr;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
