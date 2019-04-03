package com.saiyu.foreground.https.response;

import java.util.List;

public class RechargeStreamRet extends BaseRet{
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
            private String ReceiveOrderNum;
            private String CreateTime;
            private String ReserveQBCount;
            private String SuccQBCount;
            private String SuccMoney;
            private String OrderFinishTime;
            private String RechargeTime;
            private String ReceiveOrderStatus;
            private String AverageConfirmTime;
            private String ContactType;

            public String getReceiveOrderNum() {
                return ReceiveOrderNum;
            }

            public void setReceiveOrderNum(String receiveOrderNum) {
                ReceiveOrderNum = receiveOrderNum;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String createTime) {
                CreateTime = createTime;
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

            public String getSuccMoney() {
                return SuccMoney;
            }

            public void setSuccMoney(String succMoney) {
                SuccMoney = succMoney;
            }

            public String getOrderFinishTime() {
                return OrderFinishTime;
            }

            public void setOrderFinishTime(String orderFinishTime) {
                OrderFinishTime = orderFinishTime;
            }

            public String getRechargeTime() {
                return RechargeTime;
            }

            public void setRechargeTime(String rechargeTime) {
                RechargeTime = rechargeTime;
            }

            public String getReceiveOrderStatus() {
                return ReceiveOrderStatus;
            }

            public void setReceiveOrderStatus(String receiveOrderStatus) {
                ReceiveOrderStatus = receiveOrderStatus;
            }

            public String getAverageConfirmTime() {
                return AverageConfirmTime;
            }

            public void setAverageConfirmTime(String averageConfirmTime) {
                AverageConfirmTime = averageConfirmTime;
            }

            public String getContactType() {
                return ContactType;
            }

            public void setContactType(String contactType) {
                ContactType = contactType;
            }
        }
    }
}
