package com.saiyu.foreground.https.response;

import java.util.List;

public class LoginRecordRet extends BaseRet {
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private int totalCount;

        private List<ItemsBean> logList;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<ItemsBean> getLogList() {
            return logList;
        }

        public void setLogList(List<ItemsBean> logList) {
            this.logList = logList;
        }

        public static class ItemsBean {
            private String EquipmentType;
            private String operationType;
            private String property;
            private String operationIP;
            private String iPDistrict;
            private String createTime;

            public String getEquipmentType() {
                return EquipmentType;
            }

            public void setEquipmentType(String equipmentType) {
                EquipmentType = equipmentType;
            }

            public String getOperationType() {
                return operationType;
            }

            public void setOperationType(String operationType) {
                this.operationType = operationType;
            }

            public String getProperty() {
                return property;
            }

            public void setProperty(String property) {
                this.property = property;
            }

            public String getOperationIP() {
                return operationIP;
            }

            public void setOperationIP(String operationIP) {
                this.operationIP = operationIP;
            }

            public String getiPDistrict() {
                return iPDistrict;
            }

            public void setiPDistrict(String iPDistrict) {
                this.iPDistrict = iPDistrict;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }

    }
}
