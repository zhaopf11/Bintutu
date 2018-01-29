package com.zhurui.bunnymall.mine.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.CouponBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/1.
 */

public class CouponRespMsg extends BaseRespMsg {
    private String totalcount;
    private List<CouponBean> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<CouponBean> getList() {
        return list;
    }

    public void setList(List<CouponBean> list) {
        this.list = list;
    }
}
