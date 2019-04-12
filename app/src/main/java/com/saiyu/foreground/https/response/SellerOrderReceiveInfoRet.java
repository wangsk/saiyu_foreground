package com.saiyu.foreground.https.response;

import java.util.List;

public class SellerOrderReceiveInfoRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String Id;
        private String ReceiveOrderNum;
        private String ProductTypeName;
        private String CreateTime;
        private String ProductName;
        private String FinishTime;
        private String RechargeTime;
        private String ReserveAccount;
        private String RoleName;
        private String AutoConfirmTime;
        private String ReserveQBCount;
        private int IsPicConfirm;
        private String SuccQBCount;
        private String ConfirmStr;
        private int IsAgentConfirm;
        private int AgentConfirmAuditStatus;
        private String SuccMoney;
        private String AverageConfirmTime;
        private String ContactType;
        private String ContactQQ;
        private String ContactMobile;
        private String ServiceMoney;
        private String Remarks;
        private String TotalMoney;
        private int IsAllowShowContact;
        private String IsAllowShowContactStr;
        private String Pic_RechargeSucc;
        private String Pic_TradeInfo;
        private String Pic_BillRecord;

        private String OrderCancelRemarks;
        private String ReserveDiscount;
        private float OnceMinCount;
        private float LessChargeDiscount;
        private String ServiceRate;
        private String LiquidatedMoney;

        public String getLiquidatedMoney() {
            return LiquidatedMoney;
        }

        public void setLiquidatedMoney(String liquidatedMoney) {
            LiquidatedMoney = liquidatedMoney;
        }

        public String getOrderCancelRemarks() {
            return OrderCancelRemarks;
        }

        public void setOrderCancelRemarks(String orderCancelRemarks) {
            OrderCancelRemarks = orderCancelRemarks;
        }

        public String getReserveDiscount() {
            return ReserveDiscount;
        }

        public void setReserveDiscount(String reserveDiscount) {
            ReserveDiscount = reserveDiscount;
        }

        public float getOnceMinCount() {
            return OnceMinCount;
        }

        public void setOnceMinCount(float onceMinCount) {
            OnceMinCount = onceMinCount;
        }

        public float getLessChargeDiscount() {
            return LessChargeDiscount;
        }

        public void setLessChargeDiscount(float lessChargeDiscount) {
            LessChargeDiscount = lessChargeDiscount;
        }

        public String getServiceRate() {
            return ServiceRate;
        }

        public void setServiceRate(String serviceRate) {
            ServiceRate = serviceRate;
        }

        private int ProductType;
        private List<HallDetailRet.DataBean.YanBaoBean> GoldList;
        private List<HallDetailRet.DataBean.YanBaoBean> SilverList;

        public int getProductType() {
            return ProductType;
        }

        public void setProductType(int productType) {
            ProductType = productType;
        }

        public List<HallDetailRet.DataBean.YanBaoBean> getGoldList() {
            return GoldList;
        }

        public void setGoldList(List<HallDetailRet.DataBean.YanBaoBean> goldList) {
            GoldList = goldList;
        }

        public List<HallDetailRet.DataBean.YanBaoBean> getSilverList() {
            return SilverList;
        }

        public void setSilverList(List<HallDetailRet.DataBean.YanBaoBean> silverList) {
            SilverList = silverList;
        }

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

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getReceiveOrderNum() {
            return ReceiveOrderNum;
        }

        public void setReceiveOrderNum(String receiveOrderNum) {
            ReceiveOrderNum = receiveOrderNum;
        }

        public String getProductTypeName() {
            return ProductTypeName;
        }

        public void setProductTypeName(String productTypeName) {
            ProductTypeName = productTypeName;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public String getFinishTime() {
            return FinishTime;
        }

        public void setFinishTime(String finishTime) {
            FinishTime = finishTime;
        }

        public String getRechargeTime() {
            return RechargeTime;
        }

        public void setRechargeTime(String rechargeTime) {
            RechargeTime = rechargeTime;
        }

        public String getReserveAccount() {
            return ReserveAccount;
        }

        public void setReserveAccount(String reserveAccount) {
            ReserveAccount = reserveAccount;
        }

        public String getRoleName() {
            return RoleName;
        }

        public void setRoleName(String roleName) {
            RoleName = roleName;
        }

        public String getAutoConfirmTime() {
            return AutoConfirmTime;
        }

        public void setAutoConfirmTime(String autoConfirmTime) {
            AutoConfirmTime = autoConfirmTime;
        }

        public String getReserveQBCount() {
            return ReserveQBCount;
        }

        public void setReserveQBCount(String reserveQBCount) {
            ReserveQBCount = reserveQBCount;
        }

        public int getIsPicConfirm() {
            return IsPicConfirm;
        }

        public void setIsPicConfirm(int isPicConfirm) {
            IsPicConfirm = isPicConfirm;
        }

        public String getSuccQBCount() {
            return SuccQBCount;
        }

        public void setSuccQBCount(String succQBCount) {
            SuccQBCount = succQBCount;
        }

        public String getConfirmStr() {
            return ConfirmStr;
        }

        public void setConfirmStr(String confirmStr) {
            ConfirmStr = confirmStr;
        }

        public int getIsAgentConfirm() {
            return IsAgentConfirm;
        }

        public void setIsAgentConfirm(int isAgentConfirm) {
            IsAgentConfirm = isAgentConfirm;
        }

        public int getAgentConfirmAuditStatus() {
            return AgentConfirmAuditStatus;
        }

        public void setAgentConfirmAuditStatus(int agentConfirmAuditStatus) {
            AgentConfirmAuditStatus = agentConfirmAuditStatus;
        }

        public String getSuccMoney() {
            return SuccMoney;
        }

        public void setSuccMoney(String succMoney) {
            SuccMoney = succMoney;
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

        public String getContactQQ() {
            return ContactQQ;
        }

        public void setContactQQ(String contactQQ) {
            ContactQQ = contactQQ;
        }

        public String getContactMobile() {
            return ContactMobile;
        }

        public void setContactMobile(String contactMobile) {
            ContactMobile = contactMobile;
        }

        public String getServiceMoney() {
            return ServiceMoney;
        }

        public void setServiceMoney(String serviceMoney) {
            ServiceMoney = serviceMoney;
        }

        public String getRemarks() {
            return Remarks;
        }

        public void setRemarks(String remarks) {
            Remarks = remarks;
        }

        public String getTotalMoney() {
            return TotalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            TotalMoney = totalMoney;
        }

        public int getIsAllowShowContact() {
            return IsAllowShowContact;
        }

        public void setIsAllowShowContact(int isAllowShowContact) {
            IsAllowShowContact = isAllowShowContact;
        }

        public String getIsAllowShowContactStr() {
            return IsAllowShowContactStr;
        }

        public void setIsAllowShowContactStr(String isAllowShowContactStr) {
            IsAllowShowContactStr = isAllowShowContactStr;
        }

        public String getPic_RechargeSucc() {
            return Pic_RechargeSucc;
        }

        public void setPic_RechargeSucc(String pic_RechargeSucc) {
            Pic_RechargeSucc = pic_RechargeSucc;
        }

        public String getPic_TradeInfo() {
            return Pic_TradeInfo;
        }

        public void setPic_TradeInfo(String pic_TradeInfo) {
            Pic_TradeInfo = pic_TradeInfo;
        }

        public String getPic_BillRecord() {
            return Pic_BillRecord;
        }

        public void setPic_BillRecord(String pic_BillRecord) {
            Pic_BillRecord = pic_BillRecord;
        }
    }
}
