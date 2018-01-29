package com.zhurui.bunnymall.viewutils.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopf on 2017/11/6.
 */

public class ProductAttrbuteRespMsg extends BaseRespMsg {
    private ProductAttrbute list;
    private String totalcount;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public ProductAttrbute getList() {
        return list;
    }

    public void setList(ProductAttrbute list) {
        this.list = list;
    }
}
