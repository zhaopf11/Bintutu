package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.ScreenObject;

import java.util.List;

/**
 * Created by zhoux on 2017/8/25.
 */

public class ScreenResp extends BaseRespMsg {

    private List<ScreenObject> list;

    public List<ScreenObject> getList() {
        return list;
    }

    public void setList(List<ScreenObject> list) {
        this.list = list;
    }
}
