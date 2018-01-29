package com.zhurui.bunnymall.home.bean;

import java.io.Serializable;

/**
 * Created by zhoux on 2017/8/15.
 */

public class GuessProductInfo  implements Serializable{

    private String sortIndex;
    private String mainSmallImage;
    private String price;
    private String collectionItemID;
    private String name;
    private String normalPrice;
    private String productID;
    private String mainImage;
    private String flag;

    public String getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(String sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getMainSmallImage() {
        return mainSmallImage;
    }

    public void setMainSmallImage(String mainSmallImage) {
        this.mainSmallImage = mainSmallImage;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCollectionItemID() {
        return collectionItemID;
    }

    public void setCollectionItemID(String collectionItemID) {
        this.collectionItemID = collectionItemID;
    }

    public String getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(String normalPrice) {
        this.normalPrice = normalPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
