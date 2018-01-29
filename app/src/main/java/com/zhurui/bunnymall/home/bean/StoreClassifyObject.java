package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.classify.bean.SmallClassifyBean;
import com.zhurui.bunnymall.common.model.BaseBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/14.
 */

public class StoreClassifyObject extends BaseBean {

    private List<BigStoreClassifyBean> supplierAreaList;
    private List<SmallStoreClassifyBean> supplierFashionList;

    public List<BigStoreClassifyBean> getSupplierAreaList() {
        return supplierAreaList;
    }

    public void setSupplierAreaList(List<BigStoreClassifyBean> supplierAreaList) {
        this.supplierAreaList = supplierAreaList;
    }

    public List<SmallStoreClassifyBean> getSupplierFashionList() {
        return supplierFashionList;
    }

    public void setSupplierFashionList(List<SmallStoreClassifyBean> supplierFashionList) {
        this.supplierFashionList = supplierFashionList;
    }
}
