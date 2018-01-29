package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/21.
 */

public class BuyTeamDetailBean extends BaseBean {
    private List<ShoeSizeBean> shoeSizeList;
    private List<ProductImageBean> productimageList;
    private List<ProductDetailBean> productDetail;
    private List<CommentBean> comment;
    private List<FootTypeBean> userfootdata;
    private List<AttachInfoBean> attachInfo;
    private SxstrBean sxstr;

    public SxstrBean getSxstr() {
        return sxstr;
    }

    public void setSxstr(SxstrBean sxstr) {
        this.sxstr = sxstr;
    }

    public List<ShoeSizeBean> getShoeSizeList() {
        return shoeSizeList;
    }

    public void setShoeSizeList(List<ShoeSizeBean> shoeSizeList) {
        this.shoeSizeList = shoeSizeList;
    }

    public List<ProductImageBean> getProductimageList() {
        return productimageList;
    }

    public void setProductimageList(List<ProductImageBean> productimageList) {
        this.productimageList = productimageList;
    }

    public List<ProductDetailBean> getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(List<ProductDetailBean> productDetail) {
        this.productDetail = productDetail;
    }

    public List<CommentBean> getComment() {
        return comment;
    }

    public void setComment(List<CommentBean> comment) {
        this.comment = comment;
    }

    public List<FootTypeBean> getUserfootdata() {
        return userfootdata;
    }

    public void setUserfootdata(List<FootTypeBean> userfootdata) {
        this.userfootdata = userfootdata;
    }

    public List<AttachInfoBean> getAttachInfo() {
        return attachInfo;
    }

    public void setAttachInfo(List<AttachInfoBean> attachInfo) {
        this.attachInfo = attachInfo;
    }
}
