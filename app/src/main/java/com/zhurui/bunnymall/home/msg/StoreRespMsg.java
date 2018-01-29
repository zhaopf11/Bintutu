package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.StoreObject;

import java.util.List;

/**
 * Created by zhoux on 2017/8/29.
 */

public class StoreRespMsg extends BaseRespMsg {

    private List<StoreObject> list;

    public List<StoreObject> getList() {
        return list;
    }

    public void setList(List<StoreObject> list) {
        this.list = list;
    }
}
