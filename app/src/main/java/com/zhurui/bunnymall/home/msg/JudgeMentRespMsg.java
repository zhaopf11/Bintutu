package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.CommentBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/12.
 */

public class JudgeMentRespMsg extends BaseRespMsg {

    private  String totalcount;
    private List<CommentBean> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<CommentBean> getList() {
        return list;
    }

    public void setList(List<CommentBean> list) {
        this.list = list;
    }
}
