package com.zhurui.bunnymall.mine.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.AddressBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/18.
 */

public class AddressMsg extends BaseRespMsg{

    private int totalcount;
    private List<AddressBean> list;

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public List<AddressBean> getList() {
        return list;
    }

    public void setList(List<AddressBean> list) {
        this.list = list;
    }
}
