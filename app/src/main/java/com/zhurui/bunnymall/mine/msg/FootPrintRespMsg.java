package com.zhurui.bunnymall.mine.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.FootPrintBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/20.
 */

public class FootPrintRespMsg extends BaseRespMsg {

    private int totalcount;
    private List<FootPrintBean> list ;


    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public List<FootPrintBean> getList() {
        return list;
    }

    public void setList(List<FootPrintBean> list) {
        this.list = list;
    }
}
