package com.saiyu.foreground.https.response;

public class AppVersionRet extends BaseRet{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private boolean result;
        private ItemsBean SysTerminalEntity;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public ItemsBean getSysTerminalEntity() {
            return SysTerminalEntity;
        }

        public void setSysTerminalEntity(ItemsBean sysTerminalEntity) {
            SysTerminalEntity = sysTerminalEntity;
        }

        public static class ItemsBean {
            private String Id;
            private String Name;
            private String Code;
            private String Sort;
            private String Logo;
            private String Icon;
            private String Version;
            private String LinkUrl;
            private String APPID;
            private String APPSecret;
            private String MandatoryUpdateVersion;
            private String VersionChangeRemarks;

            public String getId() {
                return Id;
            }

            public void setId(String id) {
                Id = id;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            public String getCode() {
                return Code;
            }

            public void setCode(String code) {
                Code = code;
            }

            public String getSort() {
                return Sort;
            }

            public void setSort(String sort) {
                Sort = sort;
            }

            public String getLogo() {
                return Logo;
            }

            public void setLogo(String logo) {
                Logo = logo;
            }

            public String getIcon() {
                return Icon;
            }

            public void setIcon(String icon) {
                Icon = icon;
            }

            public String getVersion() {
                return Version;
            }

            public void setVersion(String version) {
                Version = version;
            }

            public String getLinkUrl() {
                return LinkUrl;
            }

            public void setLinkUrl(String linkUrl) {
                LinkUrl = linkUrl;
            }

            public String getAPPID() {
                return APPID;
            }

            public void setAPPID(String APPID) {
                this.APPID = APPID;
            }

            public String getAPPSecret() {
                return APPSecret;
            }

            public void setAPPSecret(String APPSecret) {
                this.APPSecret = APPSecret;
            }

            public String getMandatoryUpdateVersion() {
                return MandatoryUpdateVersion;
            }

            public void setMandatoryUpdateVersion(String mandatoryUpdateVersion) {
                MandatoryUpdateVersion = mandatoryUpdateVersion;
            }

            public String getVersionChangeRemarks() {
                return VersionChangeRemarks;
            }

            public void setVersionChangeRemarks(String versionChangeRemarks) {
                VersionChangeRemarks = versionChangeRemarks;
            }
        }
    }
}
