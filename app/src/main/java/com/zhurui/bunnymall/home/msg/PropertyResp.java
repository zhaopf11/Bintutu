package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.PropertyObj;

import java.util.List;

/**
 * Created by zhoux on 2017/8/24.
 */

public class PropertyResp extends BaseRespMsg {

    private List<PropertyObj> list;

    public List<PropertyObj> getList() {
        return list;
    }

    public void setList(List<PropertyObj> list) {
        this.list = list;
    }
}
