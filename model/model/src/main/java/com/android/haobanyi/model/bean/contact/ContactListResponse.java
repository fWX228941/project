package com.android.haobanyi.model.bean.contact;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/9.
 *
 * @作者: 付敏
 * @创建日期：2016/08/09
 * @邮箱：466566941@qq.com
 * @当前文件描述：联系人列表
 */

    /*
    * 约定下规范：除了ID 之外，其他字段清一色设置为string类型,两个选项的要不boolean 要不整形
    * */
public class ContactListResponse {

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
        private long ContactManageID;
        private long UserID;
        private long ProvinceID;
        private long CityID;
        private long DistrictID;
        private String ContactName;
        private int Sex;//1 女
        private String ProvinceName;
        private String CityName;
        private String DistrictName;
        private String ContactAddress;
        private String ContactPhone;
        private String ContactMobile;
        private String ContactEmail;
        private int IsDefault;


        public long getContactManageID() {
            return ContactManageID;
        }

        public void setContactManageID(long contactManageID) {
            ContactManageID = contactManageID;
        }

        public long getUserID() {
            return UserID;
        }

        public void setUserID(long userID) {
            UserID = userID;
        }

        public long getProvinceID() {
            return ProvinceID;
        }

        public void setProvinceID(long provinceID) {
            ProvinceID = provinceID;
        }

        public long getCityID() {
            return CityID;
        }

        public void setCityID(long cityID) {
            CityID = cityID;
        }

        public long getDistrictID() {
            return DistrictID;
        }

        public void setDistrictID(long districtID) {
            DistrictID = districtID;
        }

        public String getContactName() {
            return ContactName;
        }

        public void setContactName(String contactName) {
            ContactName = contactName;
        }

        public int getSex() {
            return Sex;
        }

        public void setSex(int sex) {
            Sex = sex;
        }

        public String getProvinceName() {
            return ProvinceName;
        }

        public void setProvinceName(String provinceName) {
            ProvinceName = provinceName;
        }

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String cityName) {
            CityName = cityName;
        }

        public String getDistrictName() {
            return DistrictName;
        }

        public void setDistrictName(String districtName) {
            DistrictName = districtName;
        }

        public String getContactAddress() {
            return ContactAddress;
        }

        public void setContactAddress(String contactAddress) {
            ContactAddress = contactAddress;
        }

        public String getContactPhone() {
            return ContactPhone;
        }

        public void setContactPhone(String contactPhone) {
            ContactPhone = contactPhone;
        }

        public String getContactMobile() {
            return ContactMobile;
        }

        public void setContactMobile(String contactMobile) {
            ContactMobile = contactMobile;
        }

        public String getContactEmail() {
            return ContactEmail;
        }

        public void setContactEmail(String contactEmail) {
            ContactEmail = contactEmail;
        }

        public int getIsDefault() {
            return IsDefault;
        }

        public void setIsDefault(int isDefault) {
            IsDefault = isDefault;
        }
    }
}
