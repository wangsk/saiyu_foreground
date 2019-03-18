package com.saiyu.foreground.https.response;

import java.util.List;

public class IsCountDoRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private boolean result;
        private List<String> numList;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public List<String> getNumList() {
            return numList;
        }

        public void setNumList(List<String> numList) {
            this.numList = numList;
        }
    }
}
