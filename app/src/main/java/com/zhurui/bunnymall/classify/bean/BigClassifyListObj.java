package com.zhurui.bunnymall.classify.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/18.
 */

public class BigClassifyListObj extends BaseBean {

    private List<BigClassifyBean> bigTypeListInfo;
    private List<SmallClassifyBean> smallTypeListInfo;

    public List<BigClassifyBean> getBigTypeListInfo() {
        return bigTypeListInfo;
    }

    public void setBigTypeListInfo(List<BigClassifyBean> bigTypeListInfo) {
        this.bigTypeListInfo = bigTypeListInfo;
    }

    public List<SmallClassifyBean> getSmallTypeListInfo() {
        return smallTypeListInfo;
    }

    public void setSmallTypeListInfo(List<SmallClassifyBean> smallTypeListInfo) {
        this.smallTypeListInfo = smallTypeListInfo;
    }
}
