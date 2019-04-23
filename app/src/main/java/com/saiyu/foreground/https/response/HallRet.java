package com.saiyu.foreground.https.response;

import java.util.List;

public class HallRet extends BaseRet {

    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private String ProductTypeNameAlias;
        private int OrderCount;
        private int TotalCount;
        private String HelpListUrl;
        private QueryBean Query;
        private List<ItemsBean> OrderList;
        private List<ProductItemsBean> ProductList;

        public List<ProductItemsBean> getProductList() {
            return ProductList;
        }

        public String getHelpListUrl() {
            return HelpListUrl;
        }

        public void setHelpListUrl(String helpListUrl) {
            HelpListUrl = helpListUrl;
        }

        public void setProductList(List<ProductItemsBean> productList) {
            ProductList = productList;
        }

        public String getProductTypeNameAlias() {
            return ProductTypeNameAlias;
        }

        public void setProductTypeNameAlias(String productTypeNameAlias) {
            ProductTypeNameAlias = productTypeNameAlias;
        }

        public int getOrderCount() {
            return OrderCount;
        }

        public void setOrderCount(int orderCount) {
            OrderCount = orderCount;
        }

        public int getTotalCount() {
            return TotalCount;
        }

        public void setTotalCount(int totalCount) {
            TotalCount = totalCount;
        }

        public QueryBean getQuery() {
            return Query;
        }

        public void setQuery(QueryBean query) {
            Query = query;
        }

        public List<ItemsBean> getOrderList() {
            return OrderList;
        }

        public void setOrderList(List<ItemsBean> orderList) {
            OrderList = orderList;
        }

        public static class QueryBean {
            private String pType;
            private String pId;
            private String rQBCount;
            private String rDiscount;
            private String extend;
            private String sort;
            private int page;
            private int pagesize;
            private String ordernum;

            public String getpType() {
                return pType;
            }

            public void setpType(String pType) {
                this.pType = pType;
            }

            public String getpId() {
                return pId;
            }

            public void setpId(String pId) {
                this.pId = pId;
            }

            public String getrQBCount() {
                return rQBCount;
            }

            public void setrQBCount(String rQBCount) {
                this.rQBCount = rQBCount;
            }

            public String getrDiscount() {
                return rDiscount;
            }

            public void setrDiscount(String rDiscount) {
                this.rDiscount = rDiscount;
            }

            public String getExtend() {
                return extend;
            }

            public void setExtend(String extend) {
                this.extend = extend;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getPagesize() {
                return pagesize;
            }

            public void setPagesize(int pagesize) {
                this.pagesize = pagesize;
            }

            public String getOrdernum() {
                return ordernum;
            }

            public void setOrdernum(String ordernum) {
                this.ordernum = ordernum;
            }
        }

        public static class ProductItemsBean {
            String Id;
            String Name;

            public String getId() {
                return Id;
            }

            public void setId(String id) {
                Id = id;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }
        }

        public static class ItemsBean {
            private String Id;
            private String OrderNum;
            private String OrderTitle;
            private String ReserveQBCount;
            private String RechargeQBCount;
            private boolean IsCustomerConfirmation;
            private boolean IsImgConfirmation;
            private boolean IsLessThanOriginalPrice;
            private boolean IsOrderPwd;
            private boolean IsFriendLimit;
            private int OnceMinCount;
            private String OrderRSettleTotalCount;
            private String OrderRSettleTotalMoney;
            private float VipLevel;
            private String AverageConfirmTime;
            private String ReserveDiscount;
            private int IsLock;
            private String ReserveTitle;

            public String getReserveTitle() {
                return ReserveTitle;
            }

            public void setReserveTitle(String reserveTitle) {
                ReserveTitle = reserveTitle;
            }

            public int getOnceMinCount() {
                return OnceMinCount;
            }

            public void setOnceMinCount(int onceMinCount) {
                OnceMinCount = onceMinCount;
            }

            public String getId() {
                return Id;
            }

            public void setId(String id) {
                Id = id;
            }

            public String getOrderNum() {
                return OrderNum;
            }

            public void setOrderNum(String orderNum) {
                OrderNum = orderNum;
            }

            public String getOrderTitle() {
                return OrderTitle;
            }

            public void setOrderTitle(String orderTitle) {
                OrderTitle = orderTitle;
            }

            public String getReserveQBCount() {
                return ReserveQBCount;
            }

            public void setReserveQBCount(String reserveQBCount) {
                ReserveQBCount = reserveQBCount;
            }

            public String getRechargeQBCount() {
                return RechargeQBCount;
            }

            public void setRechargeQBCount(String rechargeQBCount) {
                RechargeQBCount = rechargeQBCount;
            }

            public boolean isCustomerConfirmation() {
                return IsCustomerConfirmation;
            }

            public void setCustomerConfirmation(boolean customerConfirmation) {
                IsCustomerConfirmation = customerConfirmation;
            }

            public boolean isImgConfirmation() {
                return IsImgConfirmation;
            }

            public void setImgConfirmation(boolean imgConfirmation) {
                IsImgConfirmation = imgConfirmation;
            }

            public boolean isLessThanOriginalPrice() {
                return IsLessThanOriginalPrice;
            }

            public void setLessThanOriginalPrice(boolean lessThanOriginalPrice) {
                IsLessThanOriginalPrice = lessThanOriginalPrice;
            }

            public boolean isOrderPwd() {
                return IsOrderPwd;
            }

            public void setOrderPwd(boolean orderPwd) {
                IsOrderPwd = orderPwd;
            }

            public boolean isFriendLimit() {
                return IsFriendLimit;
            }

            public void setFriendLimit(boolean friendLimit) {
                IsFriendLimit = friendLimit;
            }

            public String getOrderRSettleTotalCount() {
                return OrderRSettleTotalCount;
            }

            public void setOrderRSettleTotalCount(String orderRSettleTotalCount) {
                OrderRSettleTotalCount = orderRSettleTotalCount;
            }

            public String getOrderRSettleTotalMoney() {
                return OrderRSettleTotalMoney;
            }

            public void setOrderRSettleTotalMoney(String orderRSettleTotalMoney) {
                OrderRSettleTotalMoney = orderRSettleTotalMoney;
            }

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

            public String getReserveDiscount() {
                return ReserveDiscount;
            }

            public void setReserveDiscount(String reserveDiscount) {
                ReserveDiscount = reserveDiscount;
            }

            public int getIsLock() {
                return IsLock;
            }

            public void setIsLock(int isLock) {
                IsLock = isLock;
            }
        }
    }
}
