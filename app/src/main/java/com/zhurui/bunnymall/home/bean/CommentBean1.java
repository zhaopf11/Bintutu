package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;
import com.zhurui.bunnymall.viewutils.SecondLineRadioButton;

/**
 * Created by zhoux on 2017/8/21.
 */

public class CommentBean1 extends BaseBean {
    private String commentContent;
    private String nick;
    private String mainImage;
    private String c_userLevelName;

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getC_userLevelName() {
        return c_userLevelName;
    }

    public void setC_userLevelName(String c_userLevelName) {
        this.c_userLevelName = c_userLevelName;
    }
}
