package com.zhurui.bunnymall.cart.bean;

import com.zhurui.bunnymall.common.model.BaseBean;
import com.zhurui.bunnymall.mine.bean.UserFootDataDetailBean;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

/**
 * Created by zhoux on 2017/9/2.
 */

public class CartProductBean extends BaseBean {

    private String cartID;
    private String productID;
    private String userID;
    private String number;
    private String skuID;
    private String cartItemTypeID;
    private String groupBuyID;
    private String colorName;
    private String sizeName;
    private String footTypeDataID;
    private String isChoiceFlag;
    private String supplierID;
    private String supplierName;
    private String issuccess;
    private String sizeID;
    private String colorID;
    private String productName;
    private String stock;
    private String price;
    private String productMainImage;
    private String fee;
    private String maxBuyNumber;
    private String minBuyNumber;
    private String customPropInfo;
    private String customPropInfoName;
    private String customFlag;
    private String shuxingPrice;
    private String brandName;
    private String brandID;
    private String xnsupplierId;
    private ProductAttrbuteSxstr dingZhiShuXing;
    private UserFootDataDetailBean footDataDetail;

    public String getXnsupplierId() {
        return xnsupplierId;
    }

    public void setXnsupplierId(String xnsupplierId) {
        this.xnsupplierId = xnsupplierId;
    }

    public UserFootDataDetailBean getFootDataDetail() {
        return footDataDetail;
    }

    public void setFootDataDetail(UserFootDataDetailBean footDataDetail) {
        this.footDataDetail = footDataDetail;
    }

    public ProductAttrbuteSxstr getDingZhiShuXing() {
        return dingZhiShuXing;
    }

    public void setDingZhiShuXing(ProductAttrbuteSxstr dingZhiShuXing) {
        this.dingZhiShuXing = dingZhiShuXing;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
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

    public String getCustomPropInfoName() {
        return customPropInfoName;
    }

    public void setCustomPropInfoName(String customPropInfoName) {
        this.customPropInfoName = customPropInfoName;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSkuID() {
        return skuID;
    }

    public void setSkuID(String skuID) {
        this.skuID = skuID;
    }

    public String getCartItemTypeID() {
        return cartItemTypeID;
    }

    public void setCartItemTypeID(String cartItemTypeID) {
        this.cartItemTypeID = cartItemTypeID;
    }

    public String getGroupBuyID() {
        return groupBuyID;
    }

    public void setGroupBuyID(String groupBuyID) {
        this.groupBuyID = groupBuyID;
    }

    public String getCustomPropInfo() {
        return customPropInfo;
    }

    public void setCustomPropInfo(String customPropInfo) {
        this.customPropInfo = customPropInfo;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getFootTypeDataID() {
        return footTypeDataID;
    }

    public void setFootTypeDataID(String footTypeDataID) {
        this.footTypeDataID = footTypeDataID;
    }

    public String getIsChoiceFlag() {
        return isChoiceFlag;
    }

    public void setIsChoiceFlag(String isChoiceFlag) {
        this.isChoiceFlag = isChoiceFlag;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getIssuccess() {
        return issuccess;
    }

    public void setIssuccess(String issuccess) {
        this.issuccess = issuccess;
    }

    public String getSizeID() {
        return sizeID;
    }

    public void setSizeID(String sizeID) {
        this.sizeID = sizeID;
    }

    public String getColorID() {
        return colorID;
    }

    public void setColorID(String colorID) {
        this.colorID = colorID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getMaxBuyNumber() {
        return maxBuyNumber;
    }

    public void setMaxBuyNumber(String maxBuyNumber) {
        this.maxBuyNumber = maxBuyNumber;
    }

    public String getMinBuyNumber() {
        return minBuyNumber;
    }

    public void setMinBuyNumber(String minBuyNumber) {
        this.minBuyNumber = minBuyNumber;
    }

    //    private String sizeid;
//    private String suppliername;
//    private String colorid;
//    private String number;
//    private String maxstock;
//    private String price;
//    private String issuccess;
//    private String deliveryfee;
//    private String productid;
//    private String maxbuynumber;
//    private String custompropertiesinfo;
//    private String custompropertiesinfoname;
//    private String deliveryname;
//    private String typeid;
//    private String name;
//    private String minbuynumber;
//    private String colorpropertiesname;
//    private String sizepropertiesname;
//    private String supplierid;
//    private String userfoottypedataid;
//    private String mainimage;
//    private String orderproductattachimage;
//
//    public void setSizeid(String sizeid) {
//        this.sizeid = sizeid;
//    }
//
//    public String getSizeid() {
//        return sizeid;
//    }
//
//    public void setSuppliername(String suppliername) {
//        this.suppliername = suppliername;
//    }
//
//    public String getSuppliername() {
//        return suppliername;
//    }
//
//    public void setColorid(String colorid) {
//        this.colorid = colorid;
//    }
//
//    public String getColorid() {
//        return colorid;
//    }
//
//    public void setNumber(String number) {
//        this.number = number;
//    }
//
//    public String getNumber() {
//        return number;
//    }
//
//    public void setMaxstock(String maxstock) {
//        this.maxstock = maxstock;
//    }
//
//    public String getMaxstock() {
//        return maxstock;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setIssuccess(String issuccess) {
//        this.issuccess = issuccess;
//    }
//
//    public String getIssuccess() {
//        return issuccess;
//    }
//
//    public void setDeliveryfee(String deliveryfee) {
//        this.deliveryfee = deliveryfee;
//    }
//
//    public String getDeliveryfee() {
//        return deliveryfee;
//    }
//
//    public void setProductid(String productid) {
//        this.productid = productid;
//    }
//
//    public String getProductid() {
//        return productid;
//    }
//
//    public void setMaxbuynumber(String maxbuynumber) {
//        this.maxbuynumber = maxbuynumber;
//    }
//
//    public String getMaxbuynumber() {
//        return maxbuynumber;
//    }
//
//    public void setCustompropertiesinfo(String custompropertiesinfo) {
//        this.custompropertiesinfo = custompropertiesinfo;
//    }
//
//    public String getCustompropertiesinfo() {
//        return custompropertiesinfo;
//    }
//
//    public void setDeliveryname(String deliveryname) {
//        this.deliveryname = deliveryname;
//    }
//
//    public String getDeliveryname() {
//        return deliveryname;
//    }
//
//    public void setTypeid(String typeid) {
//        this.typeid = typeid;
//    }
//
//    public String getTypeid() {
//        return typeid;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setMinbuynumber(String minbuynumber) {
//        this.minbuynumber = minbuynumber;
//    }
//
//    public String getMinbuynumber() {
//        return minbuynumber;
//    }
//
//    public void setColorpropertiesname(String colorpropertiesname) {
//        this.colorpropertiesname = colorpropertiesname;
//    }
//
//    public String getColorpropertiesname() {
//        return colorpropertiesname;
//    }
//
//    public String getSizepropertiesname() {
//        return sizepropertiesname;
//    }
//
//    public void setSizepropertiesname(String sizepropertiesname) {
//        this.sizepropertiesname = sizepropertiesname;
//    }
//
//    public void setSupplierid(String supplierid) {
//        this.supplierid = supplierid;
//    }
//
//    public String getSupplierid() {
//        return supplierid;
//    }
//
//    public void setUserfoottypedataid(String userfoottypedataid) {
//        this.userfoottypedataid = userfoottypedataid;
//    }
//
//    public String getUserfoottypedataid() {
//        return userfoottypedataid;
//    }
//
//    public String getMainimage() {
//        return mainimage;
//    }
//
//    public void setMainimage(String mainimage) {
//        this.mainimage = mainimage;
//    }
//
//    public String getCustompropertiesinfoname() {
//        return custompropertiesinfoname;
//    }
//
//    public void setCustompropertiesinfoname(String custompropertiesinfoname) {
//        this.custompropertiesinfoname = custompropertiesinfoname;
//    }
//
//    public String getOrderproductattachimage() {
//        return orderproductattachimage;
//    }
//
//    public void setOrderproductattachimage(String orderproductattachimage) {
//        this.orderproductattachimage = orderproductattachimage;
//    }
}
