package com.android.haobanyi.model.bean.userpoint;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/11.
 *
 * @作者: 付敏
 * @创建日期：2016/08/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：会员积分列表
 */
public class UserPointListResponse {
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
        private String Point;
        private List<ListBean> list;
        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public String getPoint() {
            return Point;
        }

        public void setPoint(String point) {
            Point = point;
        }

        public static class ListBean {
            private String TypeDesc;
            private String Integral;
            private String CreateDate;

            public String getTypeDesc() {
                return TypeDesc;
            }

            public void setTypeDesc(String typeDesc) {
                TypeDesc = typeDesc;
            }

            public String getIntegral() {
                return Integral;
            }

            public void setIntegral(String integral) {
                Integral = integral;
            }

            public String getCreateDate() {
                return CreateDate;
            }

            public void setCreateDate(String createDate) {
                CreateDate = createDate;
            }
        }
    }
}
