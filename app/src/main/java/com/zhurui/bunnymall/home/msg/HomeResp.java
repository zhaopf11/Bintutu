package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.ListObject;

import java.util.List;

/**
 * Created by zhoux on 2017/8/15.
 */

public class HomeResp extends BaseRespMsg {

   private List<ListObject> list;

    public List<ListObject> getList() {
        return list;
    }

    public void setList(List<ListObject> list) {
        this.list = list;
    }
}
