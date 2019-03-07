package com.saiyu.foreground.https.response;

import java.util.List;

public class CashTypeRet extends BaseRet {

    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {


        private List<ItemsBean> RechargeLogs;


        public static class ItemsBean {
        }
    }
}
