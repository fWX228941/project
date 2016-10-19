package com.android.haobanyi.model.bean.city;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/3.
 *
 * @作者: 付敏
 * @创建日期：2016/08/03
 * @邮箱：466566941@qq.com
 * @当前文件描述：通过getAreaList 获取的城市列表
 */
public class AreaList {
    private int code;
    private String message;

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
        private String ProvinceID;
        private String Name;
        private List<CityListBean> CityList;

        public String getProvinceID() {
            return ProvinceID;
        }

        public void setProvinceID(String ProvinceID) {
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
            private String CityID;
            private String Name;
            private String ProvinceID;
            private List<DistrictListBean> DistrictList;

            public String getCityID() {
                return CityID;
            }

            public void setCityID(String CityID) {
                this.CityID = CityID;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getProvinceID() {
                return ProvinceID;
            }

            public void setProvinceID(String ProvinceID) {
                this.ProvinceID = ProvinceID;
            }

            public List<DistrictListBean> getDistrictList() {
                return DistrictList;
            }

            public void setDistrictList(List<DistrictListBean> DistrictList) {
                this.DistrictList = DistrictList;
            }

            public static class DistrictListBean {
                private String DistrictID;
                private String Name;
                private String CityID;

                public String getDistrictID() {
                    return DistrictID;
                }

                public void setDistrictID(String DistrictID) {
                    this.DistrictID = DistrictID;
                }

                public String getName() {
                    return Name;
                }

                public void setName(String Name) {
                    this.Name = Name;
                }

                public String getCityID() {
                    return CityID;
                }

                public void setCityID(String CityID) {
                    this.CityID = CityID;
                }
            }
        }
    }
}
