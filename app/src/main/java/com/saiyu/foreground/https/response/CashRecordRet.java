package com.saiyu.foreground.https.response;

import java.util.List;

public class CashRecordRet extends BaseRet {

    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private int DataCount;

        private List<ItemsBean> WithdrawLog;

        public int getDataCount() {
            return DataCount;
        }

        public void setDataCount(int dataCount) {
            DataCount = dataCount;
        }

        public List<ItemsBean> getWithdrawLog() {
            return WithdrawLog;
        }

        public void setWithdrawLog(List<ItemsBean> withdrawLog) {
            WithdrawLog = withdrawLog;
        }

        public static class ItemsBean {
            private String Id;
            private String CreateTime;
            private String OrderNum;
            private String WithdrawWayName;
            private String WithdrawAccount;
            private String ApplyMoney;
            private String ChargeMoeny;
            private String SuccMoney;
            private String FinishTime;
            private String CurrentMoney;
            private int Status;
            private String Remarks;

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

            public String getWithdrawWayName() {
                return WithdrawWayName;
            }

            public void setWithdrawWayName(String withdrawWayName) {
                WithdrawWayName = withdrawWayName;
            }

            public String getWithdrawAccount() {
                return WithdrawAccount;
            }

            public void setWithdrawAccount(String withdrawAccount) {
                WithdrawAccount = withdrawAccount;
            }

            public String getApplyMoney() {
                return ApplyMoney;
            }

            public void setApplyMoney(String applyMoney) {
                ApplyMoney = applyMoney;
            }

            public String getChargeMoeny() {
                return ChargeMoeny;
            }

            public void setChargeMoeny(String chargeMoeny) {
                ChargeMoeny = chargeMoeny;
            }

            public String getSuccMoney() {
                return SuccMoney;
            }

            public void setSuccMoney(String succMoney) {
                SuccMoney = succMoney;
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

            public String getRemarks() {
                return Remarks;
            }

            public void setRemarks(String remarks) {
                Remarks = remarks;
            }

            public String getCurrentMoney() {
                return CurrentMoney;
            }

            public void setCurrentMoney(String currentMoney) {
                CurrentMoney = currentMoney;
            }
        }

    }
}
