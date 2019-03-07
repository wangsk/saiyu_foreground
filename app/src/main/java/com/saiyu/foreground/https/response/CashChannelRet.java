package com.saiyu.foreground.https.response;

import java.util.List;

public class CashChannelRet extends BaseRet{
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {

        private String RealName;

        public String getRealName() {
            return RealName;
        }

        public void setRealName(String realName) {
            RealName = realName;
        }

        private List<ItemsBean> WithdrawWaysList;

        public List<ItemsBean> getWithdrawWaysList() {
            return WithdrawWaysList;
        }

        public void setWithdrawWaysList(List<ItemsBean> withdrawWaysList) {
            WithdrawWaysList = withdrawWaysList;
        }

        public static class ItemsBean {
            private String Id;
            private String withdrawWayConfigCharge;
            private String PayDateStr;
            private String Name;
            private String WithdrawWayName;
            private String WithdrawAccount;
            private String MinApplyMoney;
            private String StartMoney;
            private String TopMoney;
            private String LogoPic;
            private int Type;//0其他，1支付宝，2微信
            private String ImgUrl;

            public String getId() {
                return Id;
            }

            public void setId(String id) {
                Id = id;
            }

            public String getWithdrawWayConfigCharge() {
                return withdrawWayConfigCharge;
            }

            public void setWithdrawWayConfigCharge(String withdrawWayConfigCharge) {
                this.withdrawWayConfigCharge = withdrawWayConfigCharge;
            }

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

            public String getMinApplyMoney() {
                return MinApplyMoney;
            }

            public void setMinApplyMoney(String minApplyMoney) {
                MinApplyMoney = minApplyMoney;
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

            public String getLogoPic() {
                return LogoPic;
            }

            public void setLogoPic(String logoPic) {
                LogoPic = logoPic;
            }

            public int getType() {
                return Type;
            }

            public void setType(int type) {
                Type = type;
            }

            public String getImgUrl() {
                return ImgUrl;
            }

            public void setImgUrl(String imgUrl) {
                ImgUrl = imgUrl;
            }
        }
    }
}
