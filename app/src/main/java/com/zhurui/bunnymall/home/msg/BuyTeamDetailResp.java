package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.BuyTeamDetailBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/21.
 */

public class BuyTeamDetailResp extends BaseRespMsg {
    private int totalcount;
    private List<BuyTeamDetailBean> list;

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public List<BuyTeamDetailBean> getList() {
        return list;
    }

    public void setList(List<BuyTeamDetailBean> list) {
        this.list = list;
    }
}
