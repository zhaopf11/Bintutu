package com.zhurui.bunnymall.mine.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.User;

import java.util.List;

/**
 * Created by zhoux on 2017/8/15.
 */

public class AccountLoginResp extends BaseRespMsg {

    private String token;
    private long time;
    private List<User> userinfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<User> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(List<User> userinfo) {
        this.userinfo = userinfo;
    }
}
