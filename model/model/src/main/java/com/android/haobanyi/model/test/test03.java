package com.android.haobanyi.model.test;

/**
 * Created by fWX228941 on 2016/6/29.
 *
 * @作者: 付敏
 * @创建日期：2016/06/29
 * @邮箱：466566941@qq.com
 * @当前文件描述：模拟：getShoppingCart
 */
public class test03 {


    /**
     * code : 101
     * message : 处理成功！
     * data : {"OrderPayId":"20160926144604905925870060","Amount":0.01,"OrderDesc":"标仔测试","Balance":3000000}
     */

    private int code;
    private String message;
    /**
     * OrderPayId : 20160926144604905925870060
     * Amount : 0.01
     * OrderDesc : 标仔测试
     * Balance : 3000000.0
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
        private String OrderPayId;
        private double Amount;
        private String OrderDesc;
        private double Balance;

        public String getOrderPayId() {
            return OrderPayId;
        }

        public void setOrderPayId(String OrderPayId) {
            this.OrderPayId = OrderPayId;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double Amount) {
            this.Amount = Amount;
        }

        public String getOrderDesc() {
            return OrderDesc;
        }

        public void setOrderDesc(String OrderDesc) {
            this.OrderDesc = OrderDesc;
        }

        public double getBalance() {
            return Balance;
        }

        public void setBalance(double Balance) {
            this.Balance = Balance;
        }
    }
}
