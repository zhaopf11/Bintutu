package com.zhurui.bunnymall.home.bean;

import java.util.List;

/**
 * Created by zhaopf on 2017/10/25 0025.
 */

public class SxstrBean {
    private List<ProListBean> proList;
    private List<ChimaListBean> chimaList;
    private List<ChimaListBean> mianliaoList;
    private List<ChimaListBean> yanseList;

    public List<ProListBean> getProList() {
        return proList;
    }

    public void setProList(List<ProListBean> proList) {
        this.proList = proList;
    }

    public List<ChimaListBean> getChimaList() {
        return chimaList;
    }

    public void setChimaList(List<ChimaListBean> chimaList) {
        this.chimaList = chimaList;
    }

    public List<ChimaListBean> getMianliaoList() {
        return mianliaoList;
    }

    public void setMianliaoList(List<ChimaListBean> mianliaoList) {
        this.mianliaoList = mianliaoList;
    }

    public List<ChimaListBean> getYanseList() {
        return yanseList;
    }

    public void setYanseList(List<ChimaListBean> yanseList) {
        this.yanseList = yanseList;
    }
}
