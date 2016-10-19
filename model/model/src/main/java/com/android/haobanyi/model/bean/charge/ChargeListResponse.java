package com.android.haobanyi.model.bean.charge;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/10.
 *
 * @作者: 付敏
 * @创建日期：2016/08/10
 * @邮箱：466566941@qq.com
 * @当前文件描述：充值明细和余额列表
 */
public class ChargeListResponse {

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
        /*=================GetChargeList=====================*/
        private int totalCount;
        private int pageIndex;
        private int pageSize;
        /*=================GetChargeList=====================*/

        /*=================GetUserCapital====================*/
        private String Balance; //这些细节，问题出现了一定要查找
        /*=================GetUserCapital====================*/
        private List<ListBean> list;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getBalances() {
            return Balance;
        }

        public void setBalances(String balances) {
            Balance = balances;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String ChargeDetailID;
            private String ChargeAmount;
            private String ChargeTime;
            private String ChargeWay;

            public String getChargeDetailID() {
                return ChargeDetailID;
            }

            public void setChargeDetailID(String chargeDetailID) {
                ChargeDetailID = chargeDetailID;
            }

            public String getChargeAmount() {
                return ChargeAmount;
            }

            public void setChargeAmount(String chargeAmount) {
                ChargeAmount = chargeAmount;
            }

            public String getChargeTime() {
                return ChargeTime;
            }

            public void setChargeTime(String chargeTime) {
                ChargeTime = chargeTime;
            }

            public String getChargeWay() {
                return ChargeWay;
            }

            public void setChargeWay(String chargeWay) {
                ChargeWay = chargeWay;
            }
        }


    }
}
