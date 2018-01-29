package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.FullOffProductBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/26.
 */

public class FullOffListRespMsg extends BaseRespMsg {

    private List<FullOffProductBean> list;

    public List<FullOffProductBean> getList() {
        return list;
    }

    public void setList(List<FullOffProductBean> list) {
        this.list = list;
    }
}
