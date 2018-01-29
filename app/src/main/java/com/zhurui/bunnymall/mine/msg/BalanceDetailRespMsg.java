package com.zhurui.bunnymall.mine.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.BalanceDetailBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/1.
 */

public class BalanceDetailRespMsg extends BaseRespMsg {
    private String totalcount;
    private List<BalanceDetailBean> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<BalanceDetailBean> getList() {
        return list;
    }

    public void setList(List<BalanceDetailBean> list) {
        this.list = list;
    }
}
