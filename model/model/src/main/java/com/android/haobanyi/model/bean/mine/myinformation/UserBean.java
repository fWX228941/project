package com.android.haobanyi.model.bean.mine.myinformation;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/8.
 *
 * @作者: 付敏
 * @创建日期：2016/07/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：个人信息
 * 所以说登录的时候找个方法就必须要先调用一次才行的
 */
public class UserBean {


    /**
     * code : 101
     * message : 获取成功！
     * data : [{"NickName":"张三","Sex":0,"QQ":"1233211234","Email":"466566941@qq.com","Mobile":"15527221406"}]
     */

    private int code;
    private String message;
    /**
     * NickName : 张三
     * Sex : 0
     * QQ : 1233211234
     * Email : 466566941@qq.com
     * Mobile : 15527221406
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /*======================getUserInfo========================*/
        private String NickName;
        private int Sex;
        private String QQ;
        private String Email;
        private String Mobile;
        private String Photo;
        private boolean IsHasEmail;
        private boolean IsHasMobile;
        private boolean IsHasPayPwd;//是否有支付密码 ，还有另外两个标志量
        /*======================getUserInfo========================*/
        public String getPhoto() {
            return Photo;
        }

        public void setPhoto(String photo) {
            Photo = photo;
        }

        public boolean isHasEmail() {
            return IsHasEmail;
        }

        public void setIsHasEmail(boolean isHasEmail) {
            IsHasEmail = isHasEmail;
        }

        public boolean isHasMobile() {
            return IsHasMobile;
        }

        public void setIsHasMobile(boolean isHasMobile) {
            IsHasMobile = isHasMobile;
        }

        public boolean isHasPayPwd() {
            return IsHasPayPwd;
        }

        public void setIsHasPayPwd(boolean isHasPayPwd) {
            IsHasPayPwd = isHasPayPwd;
        }




        /*======================BindUser/AuthLogin========================*/
        private Long UserID;
        private String AccessToken;
        private long ExpiresIn;
        private boolean IsBind;
        private String Rdm;
        private String HeadImgUrl;
        /*======================BindUser/AuthLogin========================*/


        public Long getUserID() {
            return UserID;
        }

        public void setUserID(Long userID) {
            UserID = userID;
        }

        public String getAccessToken() {
            return AccessToken;
        }

        public void setAccessToken(String accessToken) {
            AccessToken = accessToken;
        }

        public long getExpiresIn() {
            return ExpiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            ExpiresIn = expiresIn;
        }

        public boolean isBind() {
            return IsBind;
        }

        public void setIsBind(boolean isBind) {
            IsBind = isBind;
        }

        public String getRdm() {
            return Rdm;
        }

        public void setRdm(String rdm) {
            Rdm = rdm;
        }

        public String getHeadImgUrl() {
            return HeadImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            HeadImgUrl = headImgUrl;
        }


        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public int getSex() {
            return Sex;
        }

        public void setSex(int Sex) {
            this.Sex = Sex;
        }

        public String getQQ() {
            return QQ;
        }

        public void setQQ(String QQ) {
            this.QQ = QQ;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }
    }
}
