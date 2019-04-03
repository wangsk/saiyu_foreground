package com.saiyu.foreground.https.response;

import java.util.List;

public class ProductListRet extends BaseRet{
    private DatasBean data;

    public DatasBean getData() {
        return data;
    }

    public void setData(DatasBean data) {
        this.data = data;
    }

    public static class DatasBean {
        private List<ProductItemsBean> ProList;

        public List<ProductItemsBean> getProList() {
            return ProList;
        }

        public void setProList(List<ProductItemsBean> proList) {
            ProList = proList;
        }

        public static class ProductItemsBean {
            String pId;
            String name;
            String qbCount;
            String unitName;
            String convertCount;
            int isRole;

            public int getIsRole() {
                return isRole;
            }

            public void setIsRole(int isRole) {
                this.isRole = isRole;
            }

            public String getpId() {
                return pId;
            }

            public void setpId(String pId) {
                this.pId = pId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getQbCount() {
                return qbCount;
            }

            public void setQbCount(String qbCount) {
                this.qbCount = qbCount;
            }

            public String getUnitName() {
                return unitName;
            }

            public void setUnitName(String unitName) {
                this.unitName = unitName;
            }

            public String getConvertCount() {
                return convertCount;
            }

            public void setConvertCount(String convertCount) {
                this.convertCount = convertCount;
            }
        }
    }
}
