package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;
import com.zhurui.bunnymall.mine.bean.AddressBean;
import com.zhurui.bunnymall.mine.bean.CouponBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/31.
 */

public class ProductInfoObject extends BaseBean {

    private List<PromotionActiveBean> promotionactive;
    private List<AddressBean> addressList;
    private List<CouponBean>cardList;
    private List<PointBean> point;
    private List<ProductCheckAttrBean> xuanZhongsx;

    public List<ProductCheckAttrBean> getXuanZhongsx() {
        return xuanZhongsx;
    }

    public void setXuanZhongsx(List<ProductCheckAttrBean> xuanZhongsx) {
        this.xuanZhongsx = xuanZhongsx;
    }

    public List<PromotionActiveBean> getPromotionactive() {
        return promotionactive;
    }

    public void setPromotionactive(List<PromotionActiveBean> promotionactive) {
        this.promotionactive = promotionactive;
    }

    public List<AddressBean> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressBean> addressList) {
        this.addressList = addressList;
    }

    public List<CouponBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<CouponBean> cardList) {
        this.cardList = cardList;
    }

    public List<PointBean> getPoint() {
        return point;
    }

    public void setPoint(List<PointBean> point) {
        this.point = point;
    }
}
