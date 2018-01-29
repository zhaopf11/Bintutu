package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopengfei on 2017/9/2 0002.
 */

public class CollectionsStoreListMessageBean extends BaseRespMsg {
    private String totalcount;
    private List<CollectionsStoreBean> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<CollectionsStoreBean> getList() {
        return list;
    }

    public void setList(List<CollectionsStoreBean> list) {
        this.list = list;
    }
}
