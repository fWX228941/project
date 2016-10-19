package com.android.haobanyi.model.bean.contact;

/**
 * Created by fWX228941 on 2016/8/9.
 *
 * @作者: 付敏
 * @创建日期：2016/08/09
 * @邮箱：466566941@qq.com
 * @当前文件描述：单个联系人
 */
public class ContactBean {
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
    private String ContactPhone; // 电话
    private String ContactMobile;//手机
    private String ContactEmail;
    private int IsDefault; //1 默认

    /*===================== GetContactList 获取联系人列表 ==============================*/
    public ContactBean(long contactManageID, long userID, long provinceID, long cityID, long districtID, String
            contactName, int sex, String provinceName, String cityName, String districtName, String
                               contactAddress, String contactPhone, String contactMobile, String contactEmail, int isDefault) {
        ContactManageID = contactManageID;
        UserID = userID;
        ProvinceID = provinceID;
        CityID = cityID;
        DistrictID = districtID;
        ContactName = contactName;
        Sex = sex;
        ProvinceName = provinceName;
        CityName = cityName;
        DistrictName = districtName;
        ContactAddress = contactAddress;
        ContactPhone = contactPhone;
        ContactMobile = contactMobile;
        ContactEmail = contactEmail;
        IsDefault = isDefault;
    }
    /*===================== GetContactList 获取联系人列表 ==============================*/

    /*=====================getVerifyShoppingCart 确认订单有一个添加联系人 ==============================*/
    public ContactBean(long contactManageID, String contactName, String contactAddress, String contactMobile, String
            contactPhone, int isDefault) {
        ContactManageID = contactManageID;
        ContactName = contactName;
        ContactAddress = contactAddress;
        ContactMobile = contactMobile;
        ContactPhone = contactPhone;
        IsDefault = isDefault;
    }
    /*=====================getVerifyShoppingCart 确认订单有一个添加联系人 ==============================*/
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

    public boolean IsDefault() {
        return IsDefault == 1?true:false;
    }

    public void setIsDefault(int isDefault) {
        IsDefault = isDefault;
    }
}
