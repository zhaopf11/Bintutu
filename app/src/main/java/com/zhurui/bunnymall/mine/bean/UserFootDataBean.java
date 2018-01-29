package com.zhurui.bunnymall.mine.bean;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/3 0003.
 */

public class UserFootDataBean {
    private List<UserFootDataDetailBean> userFootDataDetail;
    private List<UserFootDataListBean> userFootDataList;

    public List<UserFootDataDetailBean> getUserFootDataDetail() {
        return userFootDataDetail;
    }

    public void setUserFootDataDetail(List<UserFootDataDetailBean> userFootDataDetail) {
        this.userFootDataDetail = userFootDataDetail;
    }

    public List<UserFootDataListBean> getUserFootDataList() {
        return userFootDataList;
    }

    public void setUserFootDataList(List<UserFootDataListBean> userFootDataList) {
        this.userFootDataList = userFootDataList;
    }
}
