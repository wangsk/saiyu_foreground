package com.saiyu.foreground.https.response;

import java.util.List;

public class RechargePointHistoryRet extends BaseRet{
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private int receivePoinCount;

        private List<ItemsBean> receivePoints;

        public int getReceivePoinCount() {
            return receivePoinCount;
        }

        public void setReceivePoinCount(int receivePoinCount) {
            this.receivePoinCount = receivePoinCount;
        }

        public List<ItemsBean> getReceivePoints() {
            return receivePoints;
        }

        public void setReceivePoints(List<ItemsBean> receivePoints) {
            this.receivePoints = receivePoints;
        }

        public static class ItemsBean {
            private String CreateTime;
            private String Point;
            private String CurrentPoint;

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String createTime) {
                CreateTime = createTime;
            }

            public String getPoint() {
                return Point;
            }

            public void setPoint(String point) {
                Point = point;
            }

            public String getCurrentPoint() {
                return CurrentPoint;
            }

            public void setCurrentPoint(String currentPoint) {
                CurrentPoint = currentPoint;
            }
        }}
}
