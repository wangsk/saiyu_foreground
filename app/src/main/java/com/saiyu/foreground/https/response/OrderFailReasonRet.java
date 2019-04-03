package com.saiyu.foreground.https.response;

public class OrderFailReasonRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String AuditRemarks;

        public String getAuditRemarks() {
            return AuditRemarks;
        }

        public void setAuditRemarks(String auditRemarks) {
            AuditRemarks = auditRemarks;
        }
    }
}
