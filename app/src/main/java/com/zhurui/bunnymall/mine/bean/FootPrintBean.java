package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/20.
 */

public class FootPrintBean extends BaseBean {

    private int browsingHistoryID;
    private long productID;
    private String time;
    private String name;
    private String mainImage;
    private float price;
    private String fashionID;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFashionID() {
        return fashionID;
    }

    public void setFashionID(String fashionID) {
        this.fashionID = fashionID;
    }

    public int getBrowsingHistoryID() {
        return browsingHistoryID;
    }

    public void setBrowsingHistoryID(int browsingHistoryID) {
        this.browsingHistoryID = browsingHistoryID;
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
