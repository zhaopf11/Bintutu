package com.zhurui.bunnymall.home.bean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/15.
 */

public class ListObject {

    private List<BannerInfo> bannerInfo ;
    private List<NewProduct> newProduct;
    private List<RecommandProduct> recommendProducts;
    private List<GuessProductInfo> guessProduct ;
    private List<BannerInfo> bannerBehindInfo;


    public List<BannerInfo> getBannerInfo() {
        return bannerInfo;
    }

    public void setBannerInfo(List<BannerInfo> bannerInfo) {
        this.bannerInfo = bannerInfo;
    }

    public List<NewProduct> getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(List<NewProduct> newProduct) {
        this.newProduct = newProduct;
    }

    public List<RecommandProduct> getRecommendProducts() {
        return recommendProducts;
    }

    public void setRecommendProducts(List<RecommandProduct> recommendProducts) {
        this.recommendProducts = recommendProducts;
    }

    public List<GuessProductInfo> getGuessProduct() {
        return guessProduct;
    }

    public void setGuessProduct(List<GuessProductInfo> guessProduct) {
        this.guessProduct = guessProduct;
    }

    public List<BannerInfo> getBannerBehindInfo() {
        return bannerBehindInfo;
    }

    public void setBannerBehindInfo(List<BannerInfo> bannerBehindInfo) {
        this.bannerBehindInfo = bannerBehindInfo;
    }
}
