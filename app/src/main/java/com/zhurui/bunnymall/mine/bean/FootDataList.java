package com.zhurui.bunnymall.mine.bean;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/16 0016.
 */

public class FootDataList {
    private List<MyFootMessageBean> foodataList;

    private List<RelateUserBean> relateUserList;

    public List<MyFootMessageBean> getFoodataList() {
        return foodataList;
    }

    public void setFoodataList(List<MyFootMessageBean> foodataList) {
        this.foodataList = foodataList;
    }

    public List<RelateUserBean> getRelateUserList() {
        return relateUserList;
    }

    public void setRelateUserList(List<RelateUserBean> relateUserList) {
        this.relateUserList = relateUserList;
    }
}
