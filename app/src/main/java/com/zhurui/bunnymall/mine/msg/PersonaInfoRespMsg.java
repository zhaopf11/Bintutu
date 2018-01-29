package com.zhurui.bunnymall.mine.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.PersonaInfoBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/22.
 */

public class PersonaInfoRespMsg extends BaseRespMsg {

    private List<PersonaInfoBean> list;

    public List<PersonaInfoBean> getList() {
        return list;
    }

    public void setList(List<PersonaInfoBean> list) {
        this.list = list;
    }
}
