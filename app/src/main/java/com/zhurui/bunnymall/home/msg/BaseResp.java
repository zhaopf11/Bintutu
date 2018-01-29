package com.zhurui.bunnymall.home.msg;

import java.io.Serializable;

/**
 * Created by zhoux on 2017/8/29.
 */

public class BaseResp  implements Serializable {

    protected int result;


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
