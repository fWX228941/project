package com.android.haobanyi.model.bean.foot;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/12.
 *
 * @作者: 付敏
 * @创建日期：2016/08/12
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class FootListResponse {
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
            private long ProductID;
            private String ProductName;
            private String MinPrice;
            private String ImagePath;


            /*=============================*/
            private long UserMessageID;
            private long IsReading;// 1 已读 其他未读
            private String MessageTypeDesc;
            private String CreateDate;
            private String MessageContext;
            /*=============================*/


            public long getUserMessageID() {
                return UserMessageID;
            }

            public void setUserMessageID(long userMessageID) {
                UserMessageID = userMessageID;
            }

            public long getIsReading() {
                return IsReading;
            }

            public void setIsReading(long isReading) {
                IsReading = isReading;
            }

            public String getMessageTypeDesc() {
                return MessageTypeDesc;
            }

            public void setMessageTypeDesc(String messageTypeDesc) {
                MessageTypeDesc = messageTypeDesc;
            }

            public String getCreateDate() {
                return CreateDate;
            }

            public void setCreateDate(String createDate) {
                CreateDate = createDate;
            }

            public String getMessageContext() {
                return MessageContext;
            }

            public void setMessageContext(String messageContext) {
                MessageContext = messageContext;
            }

            public long getProductID() {
                return ProductID;
            }

            public void setProductID(long productID) {
                ProductID = productID;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String productName) {
                ProductName = productName;
            }

            public String getMinPrice() {
                return MinPrice;
            }

            public void setMinPrice(String minPrice) {
                MinPrice = minPrice;
            }

            public String getImagePath() {
                return ImagePath;
            }

            public void setImagePath(String imagePath) {
                ImagePath = imagePath;
            }
        }


    }
}
