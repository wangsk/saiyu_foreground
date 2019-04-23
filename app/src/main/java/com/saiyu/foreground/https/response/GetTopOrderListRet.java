package com.saiyu.foreground.https.response;

import java.util.List;

public class GetTopOrderListRet extends BaseRet{

    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private List<ItemsBean> TopOrderList;

        public List<ItemsBean> getTopOrderList() {
            return TopOrderList;
        }

        public void setTopOrderList(List<ItemsBean> topOrderList) {
            TopOrderList = topOrderList;
        }

        public static class ItemsBean {
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
