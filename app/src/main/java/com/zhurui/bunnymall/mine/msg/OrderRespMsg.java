package com.zhurui.bunnymall.mine.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.OrderBean;
import com.zhurui.bunnymall.mine.bean.OrderObject;

import java.util.List;

/**
 * Created by zhoux on 2017/8/30.
 */

public class OrderRespMsg extends BaseRespMsg {
    private String totalcount;
    private List<OrderObject> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<OrderObject> getList() {
        return list;
    }

    public void setList(List<OrderObject> list) {
        this.list = list;
    }
}
