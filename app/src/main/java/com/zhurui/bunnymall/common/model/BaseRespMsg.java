
package com.zhurui.bunnymall.common.model;

import java.io.Serializable;


public class BaseRespMsg implements Serializable {
    public final static int STATUS_SUCCESS =1;
    public final static int TO_LOGIN = -2;
    public final static int ERROR = -1;
    public final static int HAS_EXIT = -4;
    public final static int NOT_EXIT=-5;
    public final static int WRONG_CODE=-6;
    public final static int TIMES_MORE=-7;
    public final static int POINTS_OVER=-8;
    public final static int WRONG_LEVEL=-9;
    public final static int NOT_ACTIVE=-10;


    protected String cmd;
    protected int result;
    protected String retmsg;

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

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }
}
