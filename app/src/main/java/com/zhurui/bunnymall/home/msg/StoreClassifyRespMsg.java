package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.StoreClassifyObject;

import java.util.List;

/**
 * Created by zhoux on 2017/9/14.
 */

public class StoreClassifyRespMsg extends BaseRespMsg {

    private List<StoreClassifyObject> list;


    public List<StoreClassifyObject> getList() {
        return list;
    }

    public void setList(List<StoreClassifyObject> list) {
        this.list = list;
    }
}
