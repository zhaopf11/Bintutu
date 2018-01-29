package com.zhurui.bunnymall.viewutils.bean;

import com.zhurui.bunnymall.common.model.BaseBean;
import com.zhurui.bunnymall.home.bean.CustomeChildPropertyBean;
import com.zhurui.bunnymall.home.bean.Product;
import com.zhurui.bunnymall.mine.bean.UserFootDataDetailBean;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaopf on 2017/11/6.
 */

public class ProductAttrbuteSxstr extends BaseBean {

    private List<CustomeChildPropertyBean> dicaiList;
    private List<CustomeChildPropertyBean> dipeiList;
    private List<CustomeChildPropertyBean> xiegenList;
    private List<CustomeChildPropertyBean> gexingList;
    private List<CustomeChildPropertyBean> chimaList;
    private List<CustomeChildPropertyBean> mianliaoList;
    private List<CustomeChildPropertyBean> yanseList;
    private List<String> productid;
    private List<String> cartid;

    private List<UserFootDataDetailBean> footdatadetail;

    public List<String> getCartId() {
        return cartid;
    }

    public void setCartId(List<String> cartId) {
        this.cartid = cartId;
    }
    public List<UserFootDataDetailBean> getFootdatadetail() {
        return footdatadetail;
    }

    public void setFootdatadetail(List<UserFootDataDetailBean> footdatadetail) {
        this.footdatadetail = footdatadetail;
    }

    public List<String> getProductid() {
        return productid;
    }

    public void setProductid(List<String> productid) {
        this.productid = productid;
    }

    private List<String[]> shuxingPriceList;

    public List<String[]> getShuxingPriceList() {
        return shuxingPriceList;
    }

    public void setShuxingPriceList(List<String[]> shuxingPriceList) {
        this.shuxingPriceList = shuxingPriceList;
    }

    public List<CustomeChildPropertyBean> getDicaiList() {
        return dicaiList;
    }

    public void setDicaiList(List<CustomeChildPropertyBean> dicaiList) {
        this.dicaiList = dicaiList;
    }

    public List<CustomeChildPropertyBean> getDipeiList() {
        return dipeiList;
    }

    public void setDipeiList(List<CustomeChildPropertyBean> dipeiList) {
        this.dipeiList = dipeiList;
    }

    public List<CustomeChildPropertyBean> getXiegenList() {
        return xiegenList;
    }

    public void setXiegenList(List<CustomeChildPropertyBean> xiegenList) {
        this.xiegenList = xiegenList;
    }

    public List<CustomeChildPropertyBean> getGexingList() {
        return gexingList;
    }

    public void setGexingList(List<CustomeChildPropertyBean> gexingList) {
        this.gexingList = gexingList;
    }

    public List<CustomeChildPropertyBean> getChimaList() {
        return chimaList;
    }

    public void setChimaList(List<CustomeChildPropertyBean> chimaList) {
        this.chimaList = chimaList;
    }

    public List<CustomeChildPropertyBean> getMianliaoList() {
        return mianliaoList;
    }

    public void setMianliaoList(List<CustomeChildPropertyBean> mianliaoList) {
        this.mianliaoList = mianliaoList;
    }

    public List<CustomeChildPropertyBean> getYanseList() {
        return yanseList;
    }

    public void setYanseList(List<CustomeChildPropertyBean> yanseList) {
        this.yanseList = yanseList;
    }

}
