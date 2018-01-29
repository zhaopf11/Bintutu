package com.zhurui.bunnymall.classify.msg;

import com.zhurui.bunnymall.classify.bean.BigClassifyListObj;
import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhoux on 2017/8/18.
 */

public class ClassifyMsg extends BaseRespMsg{
    private List<BigClassifyListObj> list;

    public List<BigClassifyListObj> getList() {
        return list;
    }

    public void setList(List<BigClassifyListObj> list) {
        this.list = list;
    }
}
