package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/26.
 */

public class FullOffShowBean extends BaseBean {

        private String showID;
        private String showImage;
        private String showName;
        private String addTime;
        private String promotionActiveTypeID;
        public void setShowID(String showID) {
            this.showID = showID;
        }
        public String getShowID() {
            return showID;
        }

        public void setShowImage(String showImage) {
            this.showImage = showImage;
        }
        public String getShowImage() {
            return showImage;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }
        public String getShowName() {
            return showName;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }
        public String getAddTime() {
            return addTime;
        }

        public void setPromotionActiveTypeID(String promotionActiveTypeID) {
            this.promotionActiveTypeID = promotionActiveTypeID;
        }
        public String getPromotionActiveTypeID() {
            return promotionActiveTypeID;
        }
}
