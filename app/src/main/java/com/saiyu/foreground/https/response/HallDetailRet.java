package com.saiyu.foreground.https.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class HallDetailRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private String Id;
        private String OrderNum;
        private String OrderTitle;
        private String ReserveTitle;
        private String ReserveQBCount;
        private String RechargeQBCount;
        private String ReceiveQBCount;
        private boolean IsCustomerConfirmation;
        private boolean IsImgConfirmation;
        private boolean IsLessThanOriginalPrice;
        private boolean IsOrderPwd;
        private boolean IsFriendLimit;
        private int OnceMinCount;
        private float ReceivePoint;
        private String OrderRSettleTotalCount;
        private String OrderRSettleTotalMoney;
        private float VipLevel;
        private String AverageConfirmTime;
        private String ReserveDiscount;
        private int IsLock;
        private boolean IsReceive;
        private String OrderRechargePointsUrl;
        private String OrderFreePointsUrl;
        private int Type;
        private List<YanBaoBean> GoldList;
        private List<YanBaoBean> SilverList;
        public static class YanBaoBean{

            public  String ProName;
            private String Value;

            public String getProName() {
                return ProName;
            }

            public void setProName(String proName) {
                ProName = proName;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String value) {
                Value = value;
            }
        }

        public List<YanBaoBean> getGoldList() {
            return GoldList;
        }

        public void setGoldList(List<YanBaoBean> goldList) {
            GoldList = goldList;
        }

        public List<YanBaoBean> getSilverList() {
            return SilverList;
        }

        public void setSilverList(List<YanBaoBean> silverList) {
            SilverList = silverList;
        }

        public int getType() {
            return Type;
        }

        public void setType(int type) {
            Type = type;
        }

        public String getOrderRechargePointsUrl() {
            return OrderRechargePointsUrl;
        }

        public void setOrderRechargePointsUrl(String orderRechargePointsUrl) {
            OrderRechargePointsUrl = orderRechargePointsUrl;
        }

        public String getOrderFreePointsUrl() {
            return OrderFreePointsUrl;
        }

        public void setOrderFreePointsUrl(String orderFreePointsUrl) {
            OrderFreePointsUrl = orderFreePointsUrl;
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

        public String getReserveTitle() {
            return ReserveTitle;
        }

        public void setReserveTitle(String reserveTitle) {
            ReserveTitle = reserveTitle;
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

        public String getReceiveQBCount() {
            return ReceiveQBCount;
        }

        public void setReceiveQBCount(String receiveQBCount) {
            ReceiveQBCount = receiveQBCount;
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

        public int getOnceMinCount() {
            return OnceMinCount;
        }

        public void setOnceMinCount(int onceMinCount) {
            OnceMinCount = onceMinCount;
        }

        public float getReceivePoint() {
            return ReceivePoint;
        }

        public void setReceivePoint(float receivePoint) {
            ReceivePoint = receivePoint;
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

        public boolean isReceive() {
            return IsReceive;
        }

        public void setReceive(boolean receive) {
            IsReceive = receive;
        }
    }
}
