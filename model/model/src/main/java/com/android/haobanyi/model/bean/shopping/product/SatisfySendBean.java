package com.android.haobanyi.model.bean.shopping.product;

/**
 * Created by fWX228941 on 2016/7/15.
 *
 * @作者: 付敏
 * @创建日期：2016/07/15
 * @邮箱：466566941@qq.com
 * @当前文件描述： 满即送
 *
"SSList":[{"SSendRuleID":25,"Price":10000.00,"Money":10.00},
{"SSendRuleID":26,"Price":111.00,"Money":11.00},
{"SSendRuleID":27,"Price":222.00,"Money":22.00},
{"SSendRuleID":28,"Price":1000.00,"Money":10.00}]
 满price 送 money
 */
public class SatisfySendBean {



        private long SSendRuleID;
        private String Price;
        private String Money;

    public SatisfySendBean(long SSendRuleID, String money, String price) {
        this.SSendRuleID = SSendRuleID;
        Price = price;
        Money = money;
    }

    public long getSSendRuleID() {
            return SSendRuleID;
        }

        public void setSSendRuleID(long SSendRuleID) {
            this.SSendRuleID = SSendRuleID;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public String getMoney() {
            return Money;
        }

        public void setMoney(String Money) {
            this.Money = Money;
        }
//    }
}
