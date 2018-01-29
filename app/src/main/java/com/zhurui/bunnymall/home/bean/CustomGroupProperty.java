package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/5.
 */

public class CustomGroupProperty extends BaseBean {
        private String sortIndex;
        private String validFlag;
        private String propTypeID;
        private String name;
        private String img;
        private String footprint;
        private String customPropertiesID;
        public void setSortIndex(String sortIndex) {
            this.sortIndex = sortIndex;
        }
        public String getSortIndex() {
            return sortIndex;
        }

        public void setValidFlag(String validFlag) {
            this.validFlag = validFlag;
        }
        public String getValidFlag() {
            return validFlag;
        }

        public void setPropTypeID(String propTypeID) {
            this.propTypeID = propTypeID;
        }
        public String getPropTypeID() {
            return propTypeID;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setImg(String img) {
            this.img = img;
        }
        public String getImg() {
            return img;
        }

        public void setFootprint(String footprint) {
            this.footprint = footprint;
        }
        public String getFootprint() {
            return footprint;
        }

        public void setCustomPropertiesID(String customPropertiesID) {
            this.customPropertiesID = customPropertiesID;
        }
        public String getCustomPropertiesID() {
            return customPropertiesID;
        }

    }
