package com.android.haobanyi.model.bean.shopping.store;

import java.util.List;

/**
 * Created by fWX228941 on 2016/6/23.
 *
 * @作者: 付敏
 * @创建日期：2016/06/23
 * @邮箱：466566941@qq.com
 * @当前文件描述：获取店铺活动列表  对应接口：GetShopActivity
 *
 * 场景一：没有数据：list为空List<?> data;
 * 场景二:shopId == 1的时候才请求成功
 * 场景三：Rules":[] 满即送为空   List<?> Rules
 *
 * 只是用来看的，不涉及到变化
 */
public class ShopActivityBean {


    /**
     * code : 101
     * message : 获取成功！
     * data : [{"Flag":3,"Id":4,"Title":"测试管理添加服务修改价格","Desc":"","StartTime":"","EndTime":"",
     * "CreateTime":"2016-04-14T15:07:58.83","Rules":[]},{"Flag":3,"Id":5,"Title":"不填写优惠价，优惠组合价格=原价","Desc":"",
     * "StartTime":"","EndTime":"","CreateTime":"2016-04-20T16:54:04.857","Rules":[]},{"Flag":3,"Id":8,
     * "Title":"最多五个服务就不能再添加了","Desc":"","StartTime":"","EndTime":"","CreateTime":"2016-04-20T16:57:29.527",
     * "Rules":[]},{"Flag":3,"Id":9,"Title":"测试价格不为数字提交警告价格要求为数字","Desc":"","StartTime":"","EndTime":"",
     * "CreateTime":"2016-04-20T17:02:22.293","Rules":[]},{"Flag":3,"Id":10,"Title":"活动名称不能为空也不能为全空格","Desc":"",
     * "StartTime":"","EndTime":"","CreateTime":"2016-04-22T15:55:37.197","Rules":[]},{"Flag":3,"Id":13,
     * "Title":"只选一项服务不能提交至少两个服务","Desc":"","StartTime":"","EndTime":"","CreateTime":"2016-04-22T16:03:30.67",
     * "Rules":[]},{"Flag":3,"Id":14,"Title":"测试服务重复添加","Desc":"","StartTime":"","EndTime":"",
     * "CreateTime":"2016-04-22T16:05:06.797","Rules":[]},{"Flag":3,"Id":15,"Title":"测试金额保留小数后几位","Desc":"",
     * "StartTime":"","EndTime":"","CreateTime":"2016-04-22T16:08:33.747","Rules":[]},{"Flag":3,"Id":17,
     * "Title":"测试删除","Desc":"","StartTime":"","EndTime":"","CreateTime":"2016-04-22T16:21:51.143","Rules":[]},
     * {"Flag":3,"Id":19,"Title":"测试删除","Desc":"","StartTime":"","EndTime":"","CreateTime":"2016-04-22T16:22:42.687",
     * "Rules":[]},{"Flag":4,"Id":34,"Title":"test","Desc":"test","StartTime":"2016-06-21 12:00:00",
     * "EndTime":"2016-07-22 12:00:00","CreateTime":"2016-06-22T17:53:13.427","Rules":[]}]
     */

    private int code;
    private String message;
    /**
     * Flag : 3
     * Id : 4
     * Title : 测试管理添加服务修改价格
     * Desc :
     * StartTime :
     * EndTime :
     * CreateTime : 2016-04-14T15:07:58.83 // 这个没有什么用
     * Rules : []
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
        private int Flag;
        private long Id;
        private String Title;
        private String Desc;
        private String StartTime;
        private String EndTime;
        private String CreateTime;
        private List<RuleBean> Rules;

        public int getFlag() {
            return Flag;
        }

        public void setFlag(int Flag) {
            this.Flag = Flag;
        }

        public long getId() {
            return Id;
        }

        public void setId(long Id) {
            this.Id = Id;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String Desc) {
            this.Desc = Desc;
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

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public List<RuleBean> getRules() {
            return Rules;
        }

        public void setRules(List<RuleBean> Rules) {
            this.Rules = Rules;
        }

        public static class RuleBean {
            private long Id;
            private long SSendRuleID;
            private long SatisfySendID;
            private String Money;
            private String Gift;
            private String Price;

            public long getId() {
                return Id;
            }

            public void setId(long id) {
                Id = id;
            }

            public long getSSendRuleID() {
                return SSendRuleID;
            }

            public void setSSendRuleID(long SSendRuleID) {
                this.SSendRuleID = SSendRuleID;
            }

            public long getSatisfySendID() {
                return SatisfySendID;
            }

            public void setSatisfySendID(long satisfySendID) {
                SatisfySendID = satisfySendID;
            }

            public String getMoney() {
                return Money;
            }

            public void setMoney(String money) {
                Money = money;
            }

            public String getGift() {
                return Gift;
            }

            public void setGift(String gift) {
                Gift = gift;
            }

            public String getPrice() {
                return Price;
            }

            public void setPrice(String price) {
                Price = price;
            }
        }
    }
}
