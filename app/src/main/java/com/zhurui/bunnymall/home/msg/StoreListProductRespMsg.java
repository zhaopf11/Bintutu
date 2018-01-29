package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.StoreListProductBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/29.
 */

public class StoreListProductRespMsg extends BaseRespMsg {
    private List<StoreListProductBean> list;

    public List<StoreListProductBean> getList() {
        return list;
    }

    public void setList(List<StoreListProductBean> list) {
        this.list = list;
    }
}
