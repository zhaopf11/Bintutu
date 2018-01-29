package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.AddCartBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/29.
 */

public class AddCartRespMsg extends BaseRespMsg {

    private String totalcount;
    private List<AddCartBean> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<AddCartBean> getList() {
        return list;
    }

    public void setList(List<AddCartBean> list) {
        this.list = list;
    }
}
