package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.FullOffObject;

import java.util.List;

/**
 * Created by zhoux on 2017/8/26.
 */

public class FullOffRespMsg extends BaseRespMsg{

    private List<FullOffObject> list;

    public List<FullOffObject> getList() {
        return list;
    }

    public void setList(List<FullOffObject> list) {
        this.list = list;
    }
}
