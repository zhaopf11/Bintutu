package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.MakeOrderBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/31.
 */

public class MakeOrderRespMsg extends BaseRespMsg {

    private List<MakeOrderBean> list ;

    public List<MakeOrderBean> getList() {
        return list;
    }

    public void setList(List<MakeOrderBean> list) {
        this.list = list;
    }
}
