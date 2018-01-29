package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.CommentBean;
import com.zhurui.bunnymall.home.bean.CustomizedDetailBean;
import com.zhurui.bunnymall.home.bean.CustomizedImageBean;

import org.w3c.dom.ls.LSException;

import java.util.List;

/**
 * Created by zhoux on 2017/8/16.
 */

public class CustomizedDeatilResp extends BaseRespMsg {
    private List<CustomizedDetailBean> list;

    private List<CustomizedImageBean> imageList;

    private List<CommentBean> commentList;


    public List<CustomizedDetailBean> getList() {
        return list;
    }

    public void setList(List<CustomizedDetailBean> list) {
        this.list = list;
    }

    public List<CustomizedImageBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<CustomizedImageBean> imageList) {
        this.imageList = imageList;
    }

    public List<CommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentBean> commentList) {
        this.commentList = commentList;
    }
}
