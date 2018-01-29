package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

/**
 * Created by zhaopf on 2017/11/28.
 */

public class ProductCheckAttrBean extends BaseBean {
    private ProductAttrbuteSxstr sxstr;

    public ProductAttrbuteSxstr getSxstr() {
        return sxstr;
    }

    public void setSxstr(ProductAttrbuteSxstr sxstr) {
        this.sxstr = sxstr;
    }
}
