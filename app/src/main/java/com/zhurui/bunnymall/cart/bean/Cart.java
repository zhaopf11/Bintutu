package com.zhurui.bunnymall.cart.bean;

/**
 * Created by zhoux on 2017/7/31.
 */

public class Cart {

    private String groupId;
    private String id;
    private boolean isCheck =false;
    private int groupPosition;
    private int childLenght ;
    private int amount;

    private float price;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public int getChildLenght() {
        return childLenght;
    }

    public void setChildLenght(int childLenght) {
        this.childLenght = childLenght;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
