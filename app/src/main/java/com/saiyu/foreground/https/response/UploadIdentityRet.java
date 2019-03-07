package com.saiyu.foreground.https.response;

public class UploadIdentityRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private boolean result;
        private String Src;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getSrc() {
            return Src;
        }

        public void setSrc(String src) {
            Src = src;
        }
    }

}
