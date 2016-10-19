package com.android.haobanyi.api.test;

import java.util.List;

/**
 * Created by fWX228941 on 2016/5/12.
 *
 * @作者: 付敏
 * @创建日期：2016/05/12
 * @邮箱：466566941@qq.com
 * @当前文件描述： 城市
 */
public class testData {


    /**
     * code : 101
     * message : 获取成功
     * data : [{"ProvinceID":1,"Name":"北京市","CityList":[{"CityID":1,"Name":"北京市","ProvinceID":1,
     * "DistrictList":[{"DistrictID":1,"Name":"东城区","CityID":1},{"DistrictID":2,"Name":"西城区","CityID":1},
     * {"DistrictID":3,"Name":"崇文区","CityID":1},{"DistrictID":4,"Name":"宣武区","CityID":1},{"DistrictID":5,
     * "Name":"朝阳区","CityID":1},{"DistrictID":6,"Name":"丰台区","CityID":1},{"DistrictID":7,"Name":"石景山区","CityID":1},
     * {"DistrictID":8,"Name":"海淀区","CityID":1},{"DistrictID":9,"Name":"门头沟区","CityID":1},{"DistrictID":10,
     * "Name":"房山区","CityID":1},{"DistrictID":11,"Name":"通州区","CityID":1},{"DistrictID":12,"Name":"顺义区","CityID":1},
     * {"DistrictID":13,"Name":"昌平区","CityID":1},{"DistrictID":14,"Name":"大兴区","CityID":1},{"DistrictID":15,
     * "Name":"怀柔区","CityID":1},{"DistrictID":16,"Name":"平谷区","CityID":1},{"DistrictID":17,"Name":"密云县","CityID":1},
     * {"DistrictID":18,"Name":"延庆县","CityID":1}]}]}]
     */

    private int code;
    private String message;
    /**
     * ProvinceID : 1
     * Name : 北京市
     * CityList : [{"CityID":1,"Name":"北京市","ProvinceID":1,"DistrictList":[{"DistrictID":1,"Name":"东城区","CityID":1},
     * {"DistrictID":2,"Name":"西城区","CityID":1},{"DistrictID":3,"Name":"崇文区","CityID":1},{"DistrictID":4,
     * "Name":"宣武区","CityID":1},{"DistrictID":5,"Name":"朝阳区","CityID":1},{"DistrictID":6,"Name":"丰台区","CityID":1},
     * {"DistrictID":7,"Name":"石景山区","CityID":1},{"DistrictID":8,"Name":"海淀区","CityID":1},{"DistrictID":9,
     * "Name":"门头沟区","CityID":1},{"DistrictID":10,"Name":"房山区","CityID":1},{"DistrictID":11,"Name":"通州区","CityID":1},
     * {"DistrictID":12,"Name":"顺义区","CityID":1},{"DistrictID":13,"Name":"昌平区","CityID":1},{"DistrictID":14,
     * "Name":"大兴区","CityID":1},{"DistrictID":15,"Name":"怀柔区","CityID":1},{"DistrictID":16,"Name":"平谷区","CityID":1},
     * {"DistrictID":17,"Name":"密云县","CityID":1},{"DistrictID":18,"Name":"延庆县","CityID":1}]}]
     */

    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private long ProvinceID;
        private String Name;
        /**
         * CityID : 1
         * Name : 北京市
         * ProvinceID : 1
         * DistrictList : [{"DistrictID":1,"Name":"东城区","CityID":1},{"DistrictID":2,"Name":"西城区","CityID":1},
         * {"DistrictID":3,"Name":"崇文区","CityID":1},{"DistrictID":4,"Name":"宣武区","CityID":1},{"DistrictID":5,
         * "Name":"朝阳区","CityID":1},{"DistrictID":6,"Name":"丰台区","CityID":1},{"DistrictID":7,"Name":"石景山区",
         * "CityID":1},{"DistrictID":8,"Name":"海淀区","CityID":1},{"DistrictID":9,"Name":"门头沟区","CityID":1},
         * {"DistrictID":10,"Name":"房山区","CityID":1},{"DistrictID":11,"Name":"通州区","CityID":1},{"DistrictID":12,
         * "Name":"顺义区","CityID":1},{"DistrictID":13,"Name":"昌平区","CityID":1},{"DistrictID":14,"Name":"大兴区",
         * "CityID":1},{"DistrictID":15,"Name":"怀柔区","CityID":1},{"DistrictID":16,"Name":"平谷区","CityID":1},
         * {"DistrictID":17,"Name":"密云县","CityID":1},{"DistrictID":18,"Name":"延庆县","CityID":1}]
         */

        private List<CityListBean> CityList;

        public long getProvinceID() {
            return ProvinceID;
        }

        public void setProvinceID(long ProvinceID) {
            this.ProvinceID = ProvinceID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public List<CityListBean> getCityList() {
            return CityList;
        }

        public void setCityList(List<CityListBean> CityList) {
            this.CityList = CityList;
        }

        public static class CityListBean {
            private long CityID;
            private String Name;
            private int ProvinceID;
            /**
             * DistrictID : 1
             * Name : 东城区
             * CityID : 1
             */

            private List<DistrictListBean> DistrictList;

            public long getCityID() {
                return CityID;
            }

            public void setCityID(long CityID) {
                this.CityID = CityID;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public int getProvinceID() {
                return ProvinceID;
            }

            public void setProvinceID(int ProvinceID) {
                this.ProvinceID = ProvinceID;
            }

            public List<DistrictListBean> getDistrictList() {
                return DistrictList;
            }

            public void setDistrictList(List<DistrictListBean> DistrictList) {
                this.DistrictList = DistrictList;
            }

            public static class DistrictListBean {
                private long DistrictID;
                private String Name;
                private int CityID;

                public long getDistrictID() {
                    return DistrictID;
                }

                public void setDistrictID(long DistrictID) {
                    this.DistrictID = DistrictID;
                }

                public String getName() {
                    return Name;
                }

                public void setName(String Name) {
                    this.Name = Name;
                }

                public int getCityID() {
                    return CityID;
                }

                public void setCityID(int CityID) {
                    this.CityID = CityID;
                }
            }
        }
    }
}
