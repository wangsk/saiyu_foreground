package com.saiyu.foreground.https.response;

import java.util.List;

public class OrderNumRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String OrderNum;
        private boolean IsNeedMobile;
        private String Mobile;
        private List<String> PostScriptList;

        public String getOrderNum() {
            return OrderNum;
        }

        public void setOrderNum(String orderNum) {
            OrderNum = orderNum;
        }

        public boolean isNeedMobile() {
            return IsNeedMobile;
        }

        public void setNeedMobile(boolean needMobile) {
            IsNeedMobile = needMobile;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public List<String> getPostScriptList() {
            return PostScriptList;
        }

        public void setPostScriptList(List<String> postScriptList) {
            PostScriptList = postScriptList;
        }
    }
}
