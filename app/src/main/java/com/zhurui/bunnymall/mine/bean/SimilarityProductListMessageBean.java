package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.Product;

import java.util.List;

/**
 * Created by zhaopengfei on 2017/9/2 0002.
 */

public class SimilarityProductListMessageBean extends BaseRespMsg {
    private String totalcount;
    private List<Product> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }
}
