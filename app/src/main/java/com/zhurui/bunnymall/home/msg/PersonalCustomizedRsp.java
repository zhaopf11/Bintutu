package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.PersonalCustomizedbean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/16.
 */

public class PersonalCustomizedRsp extends BaseRespMsg {

    private int totalcount;
    private List<PersonalCustomizedbean> list;

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public List<PersonalCustomizedbean> getList() {
        return list;
    }

    public void setList(List<PersonalCustomizedbean> list) {
        this.list = list;
    }
}
