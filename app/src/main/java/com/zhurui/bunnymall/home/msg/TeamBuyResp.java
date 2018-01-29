package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.TeamBuyBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/20.
 */

public class TeamBuyResp extends BaseRespMsg {

    private int totalcount;
    private List<TeamBuyBean> list;

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public List<TeamBuyBean> getList() {
        return list;
    }

    public void setList(List<TeamBuyBean> list) {
        this.list = list;
    }
}
