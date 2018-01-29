package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseBean;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.ProductInfoObject;

import java.util.List;

/**
 * Created by zhoux on 2017/8/31.
 */

public class ProductInfoRespMsg extends BaseRespMsg {

    private String totalcount;
    private List<ProductInfoObject> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<ProductInfoObject> getList() {
        return list;
    }

    public void setList(List<ProductInfoObject> list) {
        this.list = list;
    }
}
