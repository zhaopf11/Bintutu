package com.zhurui.bunnymall.home.bean;

import java.io.Serializable;

/**
 * Created by zhoux on 2017/9/15.
 */

public class WeChatRespMsg implements Serializable {
    public final static int TO_LOGIN = -2;
    private String cmd;
    private int result;
    private WeChatBean retmsg;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public WeChatBean getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(WeChatBean retmsg) {
        this.retmsg = retmsg;
    }
}
