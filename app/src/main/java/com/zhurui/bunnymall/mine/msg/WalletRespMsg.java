package com.zhurui.bunnymall.mine.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.WalletBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/2.
 */

public class WalletRespMsg extends BaseRespMsg {

    private List<WalletBean> list;

    public List<WalletBean> getList() {
        return list;
    }

    public void setList(List<WalletBean> list) {
        this.list = list;
    }
}
