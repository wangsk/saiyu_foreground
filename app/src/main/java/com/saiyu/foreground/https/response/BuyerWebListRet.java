package com.saiyu.foreground.https.response;

import java.util.List;

public class BuyerWebListRet extends BaseRet {
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private float VipLevel;
        private String AverageConfirmTime;
        private String UserBuyerCount;
        private String UserBuyerMoney;
        private String UserBuyerWaitConfirmOrdersCount;
        private String UserBuyerWaitConfirmOrdersMoney;
        private String UserBuyerOrderRSettleTotalCount;
        private String UserBuyerOrderRSettleTotalMoney;

        public float getVipLevel() {
            return VipLevel;
        }

        public void setVipLevel(float vipLevel) {
            VipLevel = vipLevel;
        }

        public String getAverageConfirmTime() {
            return AverageConfirmTime;
        }

        public void setAverageConfirmTime(String averageConfirmTime) {
            AverageConfirmTime = averageConfirmTime;
        }

        public String getUserBuyerCount() {
            return UserBuyerCount;
        }

        public void setUserBuyerCount(String userBuyerCount) {
            UserBuyerCount = userBuyerCount;
        }

        public String getUserBuyerMoney() {
            return UserBuyerMoney;
        }

        public void setUserBuyerMoney(String userBuyerMoney) {
            UserBuyerMoney = userBuyerMoney;
        }

        public String getUserBuyerWaitConfirmOrdersCount() {
            return UserBuyerWaitConfirmOrdersCount;
        }

        public void setUserBuyerWaitConfirmOrdersCount(String userBuyerWaitConfirmOrdersCount) {
            UserBuyerWaitConfirmOrdersCount = userBuyerWaitConfirmOrdersCount;
        }

        public String getUserBuyerWaitConfirmOrdersMoney() {
            return UserBuyerWaitConfirmOrdersMoney;
        }

        public void setUserBuyerWaitConfirmOrdersMoney(String userBuyerWaitConfirmOrdersMoney) {
            UserBuyerWaitConfirmOrdersMoney = userBuyerWaitConfirmOrdersMoney;
        }

        public String getUserBuyerOrderRSettleTotalCount() {
            return UserBuyerOrderRSettleTotalCount;
        }

        public void setUserBuyerOrderRSettleTotalCount(String userBuyerOrderRSettleTotalCount) {
            UserBuyerOrderRSettleTotalCount = userBuyerOrderRSettleTotalCount;
        }

        public String getUserBuyerOrderRSettleTotalMoney() {
            return UserBuyerOrderRSettleTotalMoney;
        }

        public void setUserBuyerOrderRSettleTotalMoney(String userBuyerOrderRSettleTotalMoney) {
            UserBuyerOrderRSettleTotalMoney = userBuyerOrderRSettleTotalMoney;
        }

        private List<ItemsBean> NewsList;

        public List<ItemsBean> getNewsList() {
            return NewsList;
        }

        public void setNewsList(List<ItemsBean> newsList) {
            NewsList = newsList;
        }

        public static class ItemsBean {
            private String Title;
            private String NewsDetailUrl;

            public String getTitle() {
                return Title;
            }

            public void setTitle(String title) {
                Title = title;
            }

            public String getNewsDetailUrl() {
                return NewsDetailUrl;
            }

            public void setNewsDetailUrl(String newsDetailUrl) {
                NewsDetailUrl = newsDetailUrl;
            }
        }
    }
}
