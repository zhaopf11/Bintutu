package com.zhurui.bunnymall.shoes.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopf on 2017/12/15.
 */

public class ShoesBean extends BaseRespMsg {
    private List<ShoesListBean> list;

    public List<ShoesListBean> getList() {
        return list;
    }

    public void setList(List<ShoesListBean> list) {
        this.list = list;
    }
}
