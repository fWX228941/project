package com.android.haobanyi.model.bean.redenvelop;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/11.
 *
 * @作者: 付敏
 * @创建日期：2016/08/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：红包列表
 */
public class RedEnvelopListResponse {

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

        private int totalCount;
        private int pageIndex;
        private int pageSize;


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

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /*===============GetRedEnvelopeList=====================*/
            private long RedEnvelopeID;
            private String Price;
            private int Status;
            /*===============GetRedEnvelopeList=====================*/

           /*===============common=====================*/
            private long RedEnvelopeTemplateID;
            private String OrderLimit;
            private String EndTime;
           /*===============common=====================*/

            /*===============GetRedEnvelopeTempList=====================*/
            private String EachLimit;
            private String RedEnvelopePrice;
            private boolean IsReceive;// 是否领取 true可以
            private boolean IsCanReceive;// 是否能领取 true
            /*===============GetRedEnvelopeTempList=====================*/
            public long getRedEnvelopeID() {
                return RedEnvelopeID;
            }

            public void setRedEnvelopeID(long redEnvelopeID) {
                RedEnvelopeID = redEnvelopeID;
            }

            public String getPrice() {
                return Price;
            }

            public void setPrice(String price) {
                Price = price;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int status) {
                Status = status;
            }

            public long getRedEnvelopeTemplateID() {
                return RedEnvelopeTemplateID;
            }

            public void setRedEnvelopeTemplateID(long redEnvelopeTemplateID) {
                RedEnvelopeTemplateID = redEnvelopeTemplateID;
            }

            public String getOrderLimit() {
                return OrderLimit;
            }

            public void setOrderLimit(String orderLimit) {
                OrderLimit = orderLimit;
            }

            public String getEndTime() {
                return EndTime;
            }

            public void setEndTime(String endTime) {
                EndTime = endTime;
            }

            public String getEachLimit() {
                return EachLimit;
            }

            public void setEachLimit(String eachLimit) {
                EachLimit = eachLimit;
            }

            public String getRedEnvelopePrice() {
                return RedEnvelopePrice;
            }

            public void setRedEnvelopePrice(String redEnvelopePrice) {
                RedEnvelopePrice = redEnvelopePrice;
            }

            public boolean isReceive() {
                return IsReceive;
            }

            public void setIsReceive(boolean isReceive) {
                IsReceive = isReceive;
            }

            public boolean isCanReceive() {
                return IsCanReceive;
            }

            public void setIsCanReceive(boolean isCanReceive) {
                IsCanReceive = isCanReceive;
            }


        }


    }

}
