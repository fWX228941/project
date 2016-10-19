package com.android.haobanyi.model.bean.voucher;

import java.util.List;

/**
 * Created by fWX228941 on 2016/6/23.
 *
 * @作者: 付敏
 * @创建日期：2016/06/23
 * @邮箱：466566941@qq.com
 * @当前文件描述：代金券列表
 */
public class VoucherListResponse {

    /**
     * code : 101
     * message : 获取成功！
     * data : [{"VouchersTemplateID":55,"Title":"红包来袭","VouchersDes":"test为","StartTime":"2016-06-22 18:10:28",
     * "EndTime":"2016-07-22 00:00:00","Price":10,"Image":""},{"VouchersTemplateID":10052,"Title":"积分兑换",
     * "VouchersDes":"则表示不限制使用代金券的消费金额","StartTime":"2016-07-06 10:16:58","EndTime":"2016-07-21 00:00:00","Price":20,
     * "Image":""},{"VouchersTemplateID":10053,"Title":"则表示不限制使用代金券的消费金额","VouchersDes":"则表示不限制使用代金券的消费金额",
     * "StartTime":"2016-07-06 10:17:23","EndTime":"2016-07-21 00:00:00","Price":20,"Image":""},
     * {"VouchersTemplateID":10054,"Title":"会员可以在积分中心用","VouchersDes":"的消费金额","StartTime":"2016-07-06 10:26:28",
     * "EndTime":"2016-07-20 00:00:00","Price":20,"Image":""},{"VouchersTemplateID":10055,"Title":"在积分中心用积",
     * "VouchersDes":"制使用代金券的消费","StartTime":"2016-07-06 10:27:59","EndTime":"2016-07-20 00:00:00","Price":20,
     * "Image":""},{"VouchersTemplateID":10059,"Title":"123","VouchersDes":"1233333","StartTime":"2016-07-06
     * 14:36:37","EndTime":"2016-07-12 00:00:00","Price":5,"Image":""},{"VouchersTemplateID":10060,"Title":"1223",
     * "VouchersDes":"132","StartTime":"2016-07-06 14:37:06","EndTime":"2016-07-12 00:00:00","Price":5,"Image":""},
     * {"VouchersTemplateID":10061,"Title":"123312","VouchersDes":"211111","StartTime":"2016-07-06 14:37:34",
     * "EndTime":"2016-07-12 00:00:00","Price":5,"Image":""}]
     */

    private int code;
    private String message;
    /**
     * VouchersTemplateID : 55
     * Title : 红包来袭
     * VouchersDes : test为
     * StartTime : 2016-06-22 18:10:28
     * EndTime : 2016-07-22 00:00:00
     * Price : 10.0
     * Image :
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
        /*===================GetShopVouchersList========================*/
        private long VouchersTemplateID;
        private String Title;
        private String VouchersDes;
        private String StartTime;
        private String Image;

        /*======================新增字段===================================*/
        private int EachLimit;// 0 不限制，每人限领
        private boolean IsExist;// true已领取，false 可领取
        /*======================新增字段===================================*/

        /*==================GetShopVouchersList=========================*/



        /*===========================公共字段===================================*/
        private String Price;
        private String EndTime;
        private String Limit;
        /*============================公共字段==================================*/




        /*===================GetVoucherList========================*/
        private long VouchersID;
        private int Status;
        private String ShopName;

        /*==================GetVoucherList=========================*/



        public int getEachLimit() {
            return EachLimit;
        }

        public void setEachLimit(int eachLimit) {
            EachLimit = eachLimit;
        }

        public boolean isExist() {
            return IsExist;
        }

        public void setExist(boolean exist) {
            IsExist = exist;
        }


        public long getVouchersID() {
            return VouchersID;
        }

        public void setVouchersID(long vouchersID) {
            VouchersID = vouchersID;
        }

        public String getLimit() {
            return Limit;
        }

        public void setLimit(String limit) {
            Limit = limit;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String shopName) {
            ShopName = shopName;
        }

        public long getVouchersTemplateID() {
            return VouchersTemplateID;
        }

        public void setVouchersTemplateID(long VouchersTemplateID) {
            this.VouchersTemplateID = VouchersTemplateID;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getVouchersDes() {
            return VouchersDes;
        }

        public void setVouchersDes(String VouchersDes) {
            this.VouchersDes = VouchersDes;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }
    }
}
