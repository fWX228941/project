package com.android.haobanyi.model.bean.shopping.product;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/6.
 *
 * @作者: 付敏
 * @创建日期：2016/07/06
 * @邮箱：466566941@qq.com
 * @当前文件描述： 评价
{"code":101,"message":"获取成功","data":{"totalCount":1,"pageIndex":1,"pageSize":10,"list":[{"AssessmentID":1,"Name":"套餐1","Views":"套餐1","AssessmentDate":"2016-01-01","ReplyList":[{"AssessmentID":1,"Name":"套餐1","Views":"套餐1","AssessmentDate":"2016-01-01"}]}]}}


 当只有一条评论时：
this.appAction.getAssessmentList(15,0,1,10); （1,10）
{"code":101,"message":"获取成功！",
"data":{"totalCount":1,"pageIndex":1,"pageSize":10,"list":[{"AssessmentID":2,"Name":"1***5","Views":"满意。5星。服务好","AssessmentDate":"2016-07-16","ReplyList":[]}]}}

this.appAction.getAssessmentList(15,0,2,10);
{"code":101,"message":"获取成功！","data":{"totalCount":1,"pageIndex":2,"pageSize":10,"list":[]}}
totalCount 总共的条数

pageIndex++

 一个是list为空的情况


 */
public class EvaluationBean {
// 0 全部 1 好，2 中，3 差
    /**
     * code : 101
     * message : 获取成功
     * data : {"totalCount":1,"pageIndex":1,"pageSize":10,"list":[{"AssessmentID":1,"Name":"套餐1","Views":"套餐1",
     * "AssessmentDate":"2016-01-01","ReplyList":[{"AssessmentID":1,"Name":"套餐1","Views":"套餐1",
     * "AssessmentDate":"2016-01-01"}]}]}
     */

    private int code;
    private String message;
    /**
     * totalCount : 1
     * pageIndex : 1
     * pageSize : 10
     * list : [{"AssessmentID":1,"Name":"套餐1","Views":"套餐1","AssessmentDate":"2016-01-01",
     * "ReplyList":[{"AssessmentID":1,"Name":"套餐1","Views":"套餐1","AssessmentDate":"2016-01-01"}]}]
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
        private int totalCount;
        private int pageIndex;
        private int pageSize;
        /**
         * AssessmentID : 1
         * Name : 套餐1
         * Views : 套餐1
         * AssessmentDate : 2016-01-01
         * ReplyList : [{"AssessmentID":1,"Name":"套餐1","Views":"套餐1","AssessmentDate":"2016-01-01"}]
         */

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
            private long AssessmentID;
            private String Name;
            private String Views;
            private String AssessmentDate;
            /**
             * AssessmentID : 1
             * Name : 套餐1
             * Views : 套餐1
             * AssessmentDate : 2016-01-01
             */

            private List<ReplyListBean> ReplyList;

            public long getAssessmentID() {
                return AssessmentID;
            }

            public void setAssessmentID(long AssessmentID) {
                this.AssessmentID = AssessmentID;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getViews() {
                return Views;
            }

            public void setViews(String Views) {
                this.Views = Views;
            }

            public String getAssessmentDate() {
                return AssessmentDate;
            }

            public void setAssessmentDate(String AssessmentDate) {
                this.AssessmentDate = AssessmentDate;
            }

            public List<ReplyListBean> getReplyList() {
                return ReplyList;
            }

            public void setReplyList(List<ReplyListBean> ReplyList) {
                this.ReplyList = ReplyList;
            }

            public static class ReplyListBean {
                private long AssessmentID;
                private String Name;
                private String Views;
                private String AssessmentDate;

                public long getAssessmentID() {
                    return AssessmentID;
                }

                public void setAssessmentID(long AssessmentID) {
                    this.AssessmentID = AssessmentID;
                }

                public String getName() {
                    return Name;
                }

                public void setName(String Name) {
                    this.Name = Name;
                }

                public String getViews() {
                    return Views;
                }

                public void setViews(String Views) {
                    this.Views = Views;
                }

                public String getAssessmentDate() {
                    return AssessmentDate;
                }

                public void setAssessmentDate(String AssessmentDate) {
                    this.AssessmentDate = AssessmentDate;
                }
            }
        }
    }
}
