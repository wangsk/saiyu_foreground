package com.saiyu.foreground.https.response;

import java.util.List;

public class ReceivePointRet extends BaseRet {
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private int ReceivePointListCount;

        private List<ItemsBean> ReceivePointList;

        public int getReceivePointListCount() {
            return ReceivePointListCount;
        }

        public void setReceivePointListCount(int receivePointListCount) {
            ReceivePointListCount = receivePointListCount;
        }

        public List<ItemsBean> getReceivePointList() {
            return ReceivePointList;
        }

        public void setReceivePointList(List<ItemsBean> receivePointList) {
            ReceivePointList = receivePointList;
        }

        public static class ItemsBean {
            private String Id;
            private String OrderNum;
            private String CreateTime;
            private String BizNote;
            private int BizType;
            private String BizTypeStr;
            private String Remarks;
            private int Type;
            private String CurrentPoint;
            private String Point;

            public String getBizTypeStr() {
                return BizTypeStr;
            }

            public void setBizTypeStr(String bizTypeStr) {
                BizTypeStr = bizTypeStr;
            }

            public String getPoint() {
                return Point;
            }

            public void setPoint(String point) {
                Point = point;
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

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String createTime) {
                CreateTime = createTime;
            }

            public String getBizNote() {
                return BizNote;
            }

            public void setBizNote(String bizNote) {
                BizNote = bizNote;
            }

            public int getBizType() {
                return BizType;
            }

            public void setBizType(int bizType) {
                BizType = bizType;
            }

            public String getRemarks() {
                return Remarks;
            }

            public void setRemarks(String remarks) {
                Remarks = remarks;
            }

            public int getType() {
                return Type;
            }

            public void setType(int type) {
                Type = type;
            }

            public String getCurrentPoint() {
                return CurrentPoint;
            }

            public void setCurrentPoint(String currentPoint) {
                CurrentPoint = currentPoint;
            }
        }
    }
}
