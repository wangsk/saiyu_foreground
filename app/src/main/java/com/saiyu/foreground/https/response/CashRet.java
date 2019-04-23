package com.saiyu.foreground.https.response;

import java.util.List;

public class CashRet extends BaseRet {

    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private boolean IsMobileVerification;
        private String TotalMoney;

        public String getTotalMoney() {
            return TotalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            TotalMoney = totalMoney;
        }

        public boolean isMobileVerification() {
            return IsMobileVerification;
        }

        public void setMobileVerification(boolean mobileVerification) {
            IsMobileVerification = mobileVerification;
        }

        private List<ItemsBean> withdrawWayAccountList;

        public List<ItemsBean> getWithdrawWayAccountList() {
            return withdrawWayAccountList;
        }

        public void setWithdrawWayAccountList(List<ItemsBean> withdrawWayAccountList) {
            this.withdrawWayAccountList = withdrawWayAccountList;
        }

        public static class ItemsBean {
            private String Id;
            private int IsDefault;
            private String WithdrawWayId;
            private String StartMoney;
            private String TopMoney;
            private String MinApplyMoney;
            private String LogoPic;
            private String Remarks;
            private String Account;
            private String Charge;

            private String Name;
            private String PayDateStr;

            public String getPayDateStr() {
                return PayDateStr;
            }

            public void setPayDateStr(String payDateStr) {
                PayDateStr = payDateStr;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            public String getId() {
                return Id;
            }

            public void setId(String id) {
                Id = id;
            }

            public int getIsDefault() {
                return IsDefault;
            }

            public void setIsDefault(int isDefault) {
                IsDefault = isDefault;
            }

            public String getWithdrawWayId() {
                return WithdrawWayId;
            }

            public void setWithdrawWayId(String withdrawWayId) {
                WithdrawWayId = withdrawWayId;
            }

            public String getStartMoney() {
                return StartMoney;
            }

            public void setStartMoney(String startMoney) {
                StartMoney = startMoney;
            }

            public String getTopMoney() {
                return TopMoney;
            }

            public void setTopMoney(String topMoney) {
                TopMoney = topMoney;
            }

            public String getMinApplyMoney() {
                return MinApplyMoney;
            }

            public void setMinApplyMoney(String minApplyMoney) {
                MinApplyMoney = minApplyMoney;
            }

            public String getLogoPic() {
                return LogoPic;
            }

            public void setLogoPic(String logoPic) {
                LogoPic = logoPic;
            }

            public String getRemarks() {
                return Remarks;
            }

            public void setRemarks(String remarks) {
                Remarks = remarks;
            }

            public String getAccount() {
                return Account;
            }

            public void setAccount(String account) {
                Account = account;
            }

            public String getCharge() {
                return Charge;
            }

            public void setCharge(String charge) {
                Charge = charge;
            }
        }
    }
}
