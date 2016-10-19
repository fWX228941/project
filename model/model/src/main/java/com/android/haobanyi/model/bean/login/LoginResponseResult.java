package com.android.haobanyi.model.bean.login;

/**
 * Created by fWX228941 on 2016/4/20.
 *
 * @作者: 付敏
 * @创建日期：2016/04/20
 * @邮箱：466566941@qq.com
 * @当前文件描述：调用login登录后的返回结果，注意在进行json格式化处理时，键不要带冒号
 * 格式：
 *      {
            code: 101, //返回码
            message: "登录成功",  //返回信息
            data:
                {
                    UserID : “10000”,  // 用户ID
                    AccessToken : ”1233211234567”, // token字段
                    ExpiresIn:7200 // token保存时间
                }
        }
 */
public class LoginResponseResult {
    private int code;
    private String message;
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
        private Long UserID;
        private String AccessToken;
        private long ExpiresIn;

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getGoodCount() {
            return goodCount;
        }

        public void setGoodCount(String goodCount) {
            this.goodCount = goodCount;
        }

        public String getMiddleCount() {
            return middleCount;
        }

        public void setMiddleCount(String middleCount) {
            this.middleCount = middleCount;
        }

        public String getBadCount() {
            return badCount;
        }

        public void setBadCount(String badCount) {
            this.badCount = badCount;
        }

        /*================getAssessmentCount========================*/
        private String totalCount;
        private String goodCount;
        private String middleCount;
        private String badCount;
        /*================getAssessmentCount========================*/

        /*================AuthLogin========================*/
        private String Rdm;
        private String HeadImgUrl;
        private String NickName;
        private boolean IsBind;
        /*================AuthLogin========================*/


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

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public boolean isBind() {
            return IsBind;
        }

        public void setIsBind(boolean isBind) {
            IsBind = isBind;
        }

        public double getBalance() {
            return Balance;
        }

        public void setBalance(double balance) {
            Balance = balance;
        }

        public String getOrderDesc() {
            return OrderDesc;
        }

        public void setOrderDesc(String orderDesc) {
            OrderDesc = orderDesc;
        }

        public String getAmount() {
            return Amount;
        }

        public void setAmount(String amount) {
            Amount = amount;
        }

        public String getOrderPayId() {
            return OrderPayId;
        }

        public void setOrderPayId(String orderPayId) {
            OrderPayId = orderPayId;
        }

        /*================SubmitPayment========================*/
        private double Balance; //用户资金余额
        private String OrderDesc; //订单描述
        private String Amount; //支付金额
        private String OrderPayId; // 支付单号ID
        /*================SubmitPayment========================*/


        public Long getUserID() {
            return UserID;
        }

        public void setUserID(Long UserID) {
            this.UserID = UserID;
        }

        public String getAccessToken() {
            return AccessToken;
        }

        public void setAccessToken(String AccessToken) {
            this.AccessToken = AccessToken;
        }

        public long getExpiresIn() {
            return ExpiresIn;
        }

        public void setExpiresIn(long ExpiresIn) {
            this.ExpiresIn = ExpiresIn;
        }
    }
}
