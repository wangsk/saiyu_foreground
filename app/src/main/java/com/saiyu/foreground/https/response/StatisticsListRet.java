package com.saiyu.foreground.https.response;

import java.util.List;

public class StatisticsListRet extends BaseRet{
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private List<ItemsBean> List;
        private List<ItemsBean_2> TopOrderList;

        public java.util.List<ItemsBean> getList() {
            return List;
        }

        public void setList(java.util.List<ItemsBean> list) {
            List = list;
        }

        public java.util.List<ItemsBean_2> getTopOrderList() {
            return TopOrderList;
        }

        public void setTopOrderList(java.util.List<ItemsBean_2> topOrderList) {
            TopOrderList = topOrderList;
        }

        public static class ItemsBean {
            private String Date;
            private int AverageDiscount;
            private int MaxDiscount;

            public String getDate() {
                return Date;
            }

            public void setDate(String date) {
                Date = date;
            }

            public int getAverageDiscount() {
                return AverageDiscount;
            }

            public void setAverageDiscount(int averageDiscount) {
                AverageDiscount = averageDiscount;
            }

            public int getMaxDiscount() {
                return MaxDiscount;
            }

            public void setMaxDiscount(int maxDiscount) {
                MaxDiscount = maxDiscount;
            }
        }
        public static class ItemsBean_2 {
            private String Id;
            private String OrderTitle;

            public String getId() {
                return Id;
            }

            public void setId(String id) {
                Id = id;
            }

            public String getOrderTitle() {
                return OrderTitle;
            }

            public void setOrderTitle(String orderTitle) {
                OrderTitle = orderTitle;
            }
        }
    }
}
