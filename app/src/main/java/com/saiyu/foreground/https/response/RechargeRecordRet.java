package com.saiyu.foreground.https.response;

import java.util.List;

public class RechargeRecordRet  extends BaseRet {
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private int DataCount;

        private List<ItemsBean> RechargeLogs;

        public int getDataCount() {
            return DataCount;
        }

        public void setDataCount(int dataCount) {
            DataCount = dataCount;
        }

        public List<ItemsBean> getRechargeLogs() {
            return RechargeLogs;
        }

        public void setRechargeLogs(List<ItemsBean> rechargeLogs) {
            RechargeLogs = rechargeLogs;
        }

        public static class ItemsBean {
            private String Id;
            private String CreateTime;
            private String OrderNum;
            private String PayOrderNum;
            private String PayTypeName;
            private String ApplyMoney;
            private String SuccMoney;
            private String CurrentMoney;
            private String FinishTime;
            private int Status;

            public String getId() {
                return Id;
            }

            public void setId(String id) {
                Id = id;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String createTime) {
                CreateTime = createTime;
            }

            public String getOrderNum() {
                return OrderNum;
            }

            public void setOrderNum(String orderNum) {
                OrderNum = orderNum;
            }

            public String getPayOrderNum() {
                return PayOrderNum;
            }

            public void setPayOrderNum(String payOrderNum) {
                PayOrderNum = payOrderNum;
            }

            public String getPayTypeName() {
                return PayTypeName;
            }

            public void setPayTypeName(String payTypeName) {
                PayTypeName = payTypeName;
            }

            public String getApplyMoney() {
                return ApplyMoney;
            }

            public void setApplyMoney(String applyMoney) {
                ApplyMoney = applyMoney;
            }

            public String getSuccMoney() {
                return SuccMoney;
            }

            public void setSuccMoney(String succMoney) {
                SuccMoney = succMoney;
            }

            public String getCurrentMoney() {
                return CurrentMoney;
            }

            public void setCurrentMoney(String currentMoney) {
                CurrentMoney = currentMoney;
            }

            public String getFinishTime() {
                return FinishTime;
            }

            public void setFinishTime(String finishTime) {
                FinishTime = finishTime;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int status) {
                Status = status;
            }
        }

    }
}
