package com.saiyu.foreground.https.response;

public class FaceRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private boolean result;
        private String faceurl;
        private String faceOrderNum;

        public String getFaceOrderNum() {
            return faceOrderNum;
        }

        public void setFaceOrderNum(String faceOrderNum) {
            this.faceOrderNum = faceOrderNum;
        }

        public String getFaceurl() {
            return faceurl;
        }

        public void setFaceurl(String faceurl) {
            this.faceurl = faceurl;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }
    }
}
