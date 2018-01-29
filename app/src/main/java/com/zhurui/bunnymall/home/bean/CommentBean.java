package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;
import com.zhurui.bunnymall.viewutils.SecondLineRadioButton;

/**
 * Created by zhoux on 2017/8/20.
 */

public class CommentBean extends BaseBean {

    private String commentID;
    private String orderProductID;
    private String userID;
    private String showUserName;
    private String orderTime;
    private String productID;
    private String price;
    private String number;
    private String propName;
    private String commentScore;
    private String postTime;
    private String commentContent;
    private String replyContent;
    private String replyTime;
    private String replyFlag;
    private String siteManagerID;
    private String validFlag;
    private String  imageNames;
    private String mainImage;
    private String umainImage;

    public String getUmainImage() {
        return umainImage;
    }

    public void setUmainImage(String umainImage) {
        this.umainImage = umainImage;
    }

    public String getShowUserName() {
        return showUserName;
    }

    public void setShowUserName(String showUserName) {
        this.showUserName = showUserName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }


    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }


    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }


    public String getSiteManagerID() {
        return siteManagerID;
    }

    public void setSiteManagerID(String siteManagerID) {
        this.siteManagerID = siteManagerID;
    }


    public String getImageNames() {
        return imageNames;
    }

    public void setImageNames(String imageNames) {
        this.imageNames = imageNames;
    }


    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getOrderProductID() {
        return orderProductID;
    }

    public void setOrderProductID(String orderProductID) {
        this.orderProductID = orderProductID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCommentScore() {
        return commentScore;
    }

    public void setCommentScore(String commentScore) {
        this.commentScore = commentScore;
    }

    public String getReplyFlag() {
        return replyFlag;
    }

    public void setReplyFlag(String replyFlag) {
        this.replyFlag = replyFlag;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }
}
