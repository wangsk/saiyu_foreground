package com.saiyu.foreground.https.response;

public class FaceStatusRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int faceAuthStatus;//0:正在申请，1：申请通过，2，申请失败,3:没有申请记录
        private boolean IsModify;

        public boolean isModify() {
            return IsModify;
        }

        public void setModify(boolean modify) {
            IsModify = modify;
        }

        public int getFaceAuthStatus() {
            return faceAuthStatus;
        }

        public void setFaceAuthStatus(int faceAuthStatus) {
            this.faceAuthStatus = faceAuthStatus;
        }
    }
}
