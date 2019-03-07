package com.saiyu.foreground.https.response;

import java.util.List;

public class CashDetailRet extends BaseRet{

    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private int DataCount;

        private List<ItemsBean> RecordLogs;

        public int getDataCount() {
            return DataCount;
        }

        public void setDataCount(int dataCount) {
            DataCount = dataCount;
        }

        public List<ItemsBean> getRecordLogs() {
            return RecordLogs;
        }

        public void setRecordLogs(List<ItemsBean> recordLogs) {
            RecordLogs = recordLogs;
        }

        public static class ItemsBean {
            private String Id;
            private String CreateTime;
            private String BizType;
            private String BizNote;
            private String OrderNum;
            private int Type;
            private String CurrentMoney;
            private String Money;
            private String WithdrawName;

            public String getWithdrawName() {
                return WithdrawName;
            }

            public void setWithdrawName(String withdrawName) {
                WithdrawName = withdrawName;
            }

            public String getMoney() {
                return Money;
            }

            public void setMoney(String money) {
                Money = money;
            }

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

            public String getBizType() {
                return BizType;
            }

            public void setBizType(String bizType) {
                BizType = bizType;
            }

            public String getBizNote() {
                return BizNote;
            }

            public void setBizNote(String bizNote) {
                BizNote = bizNote;
            }

            public String getOrderNum() {
                return OrderNum;
            }

            public void setOrderNum(String orderNum) {
                OrderNum = orderNum;
            }

            public int getType() {
                return Type;
            }

            public void setType(int type) {
                Type = type;
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
