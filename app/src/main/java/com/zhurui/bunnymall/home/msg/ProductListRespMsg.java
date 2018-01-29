package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.Banner;
import com.zhurui.bunnymall.home.bean.NewProduct;
import com.zhurui.bunnymall.home.bean.Product;

import java.util.List;

/**
 * Created by zhoux on 2017/9/13.
 */

public class ProductListRespMsg extends BaseRespMsg {

    private String totalcount;
    private List<Product> list;
    private List<Banner> listbanner;

    public List<Banner> getListbanner() {
        return listbanner;
    }

    public void setListbanner(List<Banner> listbanner) {
        this.listbanner = listbanner;
    }

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
