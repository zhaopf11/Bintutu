package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/12 0012.
 */

public class EnterMallListBean extends BaseRespMsg{
    private List<EnterMallBean> list;

    public List<EnterMallBean> getList() {
        return list;
    }

    public void setList(List<EnterMallBean> list) {
        this.list = list;
    }
}
