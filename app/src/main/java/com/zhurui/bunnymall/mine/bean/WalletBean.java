package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseBean;
import com.zhurui.bunnymall.common.model.BaseRespMsg;

/**
 * Created by zhoux on 2017/9/2.
 */

public class WalletBean extends BaseBean {


        private String userMoney;
        private String point;
        public void setUserMoney(String userMoney) {
            this.userMoney = userMoney;
        }
        public String getUserMoney() {
            return userMoney;
        }

        public void setPoint(String point) {
            this.point = point;
        }
        public String getPoint() {
            return point;
        }
}
