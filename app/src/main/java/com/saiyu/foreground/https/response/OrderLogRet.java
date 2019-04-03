package com.saiyu.foreground.https.response;

import java.util.List;

public class OrderLogRet extends BaseRet{

    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private String orderEntity;
        private String ProductName;
        private String ReserveQBCount;
        private String SuccQBCount;
        private String RemainingAmount;
        private List<ItemsBean> orderReceiveList;

        public String getOrderEntity() {
            return orderEntity;
        }

        public void setOrderEntity(String orderEntity) {
            this.orderEntity = orderEntity;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public String getReserveQBCount() {
            return ReserveQBCount;
        }

        public void setReserveQBCount(String reserveQBCount) {
            ReserveQBCount = reserveQBCount;
        }

        public String getSuccQBCount() {
            return SuccQBCount;
        }

        public void setSuccQBCount(String succQBCount) {
            SuccQBCount = succQBCount;
        }

        public String getRemainingAmount() {
            return RemainingAmount;
        }

        public void setRemainingAmount(String remainingAmount) {
            RemainingAmount = remainingAmount;
        }

        public List<ItemsBean> getOrderReceiveList() {
            return orderReceiveList;
        }

        public void setOrderReceiveList(List<ItemsBean> orderReceiveList) {
            this.orderReceiveList = orderReceiveList;
        }

        public static class ItemsBean {
            private String CreateTime;
            private String UserAccount;
            private String Remarks;

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String createTime) {
                CreateTime = createTime;
            }

            public String getUserAccount() {
                return UserAccount;
            }

            public void setUserAccount(String userAccount) {
                UserAccount = userAccount;
            }

            public String getRemarks() {
                return Remarks;
            }

            public void setRemarks(String remarks) {
                Remarks = remarks;
            }
        }
    }
}
