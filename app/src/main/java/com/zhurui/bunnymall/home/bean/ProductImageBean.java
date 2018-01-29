package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/21.
 */

public class ProductImageBean extends BaseBean {
    private String image;
    private int mainFlag;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMainFlag() {
        return mainFlag;
    }

    public void setMainFlag(int mainFlag) {
        this.mainFlag = mainFlag;
    }
}
