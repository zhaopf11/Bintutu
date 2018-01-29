package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseBean;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

/**
 * Created by zhaopf on 2017/8/30.
 */

public class OrderBean extends BaseBean {

    private String orderProductID;
    private String sizePropertiesName;
    private String colorPropertiesName;
    private String productID;
    private String shopOrderID;
    private String name;
    private String mainImage;
    private String supplierID;
    private String xnSupplierID;
    private String number;
    private String price;
    private String customFlag;
    private String shuxingPrice;
    private ProductAttrbuteSxstr dingZhiShuXing;

    public ProductAttrbuteSxstr getDingZhiShuXing() {
        return dingZhiShuXing;
    }

    public void setDingZhiShuXing(ProductAttrbuteSxstr dingZhiShuXing) {
        this.dingZhiShuXing = dingZhiShuXing;
    }

    public String getShuxingPrice() {
        return shuxingPrice;
    }

    public void setShuxingPrice(String shuxingPrice) {
        this.shuxingPrice = shuxingPrice;
    }

    public String getCustomFlag() {
        return customFlag;
    }

    public void setCustomFlag(String customFlag) {
        this.customFlag = customFlag;
    }

    public String getOrderProductID() {
        return orderProductID;
    }

    public void setOrderProductID(String orderProductID) {
        this.orderProductID = orderProductID;
    }

    public String getSizePropertiesName() {
        return sizePropertiesName;
    }

    public void setSizePropertiesName(String sizePropertiesName) {
        this.sizePropertiesName = sizePropertiesName;
    }

    public String getColorPropertiesName() {
        return colorPropertiesName;
    }

    public void setColorPropertiesName(String colorPropertiesName) {
        this.colorPropertiesName = colorPropertiesName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getShopOrderID() {
        return shopOrderID;
    }

    public void setShopOrderID(String shopOrderID) {
        this.shopOrderID = shopOrderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getXnSupplierID() {
        return xnSupplierID;
    }

    public void setXnSupplierID(String xnSupplierID) {
        this.xnSupplierID = xnSupplierID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
