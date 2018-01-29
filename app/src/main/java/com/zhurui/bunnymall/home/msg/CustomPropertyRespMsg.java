package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.CustomPropertyObject;

import java.util.List;

/**
 * Created by zhoux on 2017/9/5.
 */

public class CustomPropertyRespMsg extends BaseRespMsg {

    private List<CustomPropertyObject> list;

    public List<CustomPropertyObject> getList() {
        return list;
    }

    public void setList(List<CustomPropertyObject> list) {
        this.list = list;
    }
}
