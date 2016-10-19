package com.android.haobanyi.model.bean.shopping.order;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/26.
 *
 * @作者: 付敏
 * @创建日期：2016/07/26
 * @邮箱：466566941@qq.com
 * @当前文件描述：添加购物车，post 参数
 */
public class Carts {
    List<Cartbean> carts;// 添加到购物车

    List<Cartbean01> carts01;//删除服务
    List<Cartbean02> carts02;//删除服务
    List<Cartbean03> carts03;//移动服务到收藏夹
    List<Cartbean04> carts04; //删除全部购物车
    public List<Cartbean04> getCarts04() {
        return carts04;
    }

    public void setCarts04(List<Cartbean04> carts04) {
        this.carts04 = carts04;
    }


    public List<Cartbean03> getCarts03() {
        return carts03;
    }

    public void setCarts03(List<Cartbean03> carts03) {
        this.carts03 = carts03;
    }


    public List<Cartbean01> getCarts01() {
        return carts01;
    }

    public void setCarts01(List<Cartbean01> carts01) {
        this.carts01 = carts01;
    }

    public List<Cartbean> getCarts() {
        return carts;
    }

    public void setCarts(List<Cartbean> carts) {
        this.carts = carts;
    }

    public List<Cartbean02> getCarts02() {
        return carts02;
    }

    public void setCarts02(List<Cartbean02> carts02) {
        this.carts02 = carts02;
    }
//如果是post 是不是需要序列化
    public static class Cartbean implements Parcelable {


        long ShopId;
        long ProductId;
        long GroupSellID;
        long DistrictID; // 服务区域ID
        int Quantity;
        List<Long> AttributeIdList;
        public Cartbean(long shopId, long productId, long groupSellID, int quantity,long districtID, List<Long>
                attributeIdList) {
            ShopId = shopId;
            ProductId = productId;
            GroupSellID = groupSellID;
            DistrictID = districtID;
            Quantity = quantity;
            AttributeIdList = attributeIdList;
        }
        public Cartbean(long productId, long groupSellID) {
            ProductId = productId;
            GroupSellID = groupSellID;
        }
        public Cartbean(long productId) {
            ProductId = productId;
        }


        public long getDistrictID() {
            return DistrictID;
        }

        public void setDistrictID(long districtID) {
            DistrictID = districtID;
        }


        public Cartbean(long shopId, long productId, long groupSellID, int quantity, List<Long> attributeIdList) {
            ShopId = shopId;
            ProductId = productId;
            GroupSellID = groupSellID;
            Quantity = quantity;
            AttributeIdList = attributeIdList;
        }

        public long getShopId() {
            return ShopId;
        }

        public void setShopId(long shopId) {
            ShopId = shopId;
        }

        public long getProductId() {
            return ProductId;
        }

        public void setProductId(long productId) {
            ProductId = productId;
        }

        public long getGroupSellID() {
            return GroupSellID;
        }

        public void setGroupSellID(long groupSellID) {
            GroupSellID = groupSellID;
        }

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int quantity) {
            Quantity = quantity;
        }

        public List<Long> getAttributeIdList() {
            return AttributeIdList;
        }

        public void setAttributeIdList(List<Long> attributeIdList) {
            AttributeIdList = attributeIdList;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.ShopId);
            dest.writeLong(this.ProductId);
            dest.writeLong(this.GroupSellID);
            dest.writeLong(this.DistrictID);
            dest.writeInt(this.Quantity);

        }

        protected Cartbean(Parcel in) {
            this.ShopId = in.readLong();
            this.ProductId = in.readLong();
            this.GroupSellID = in.readLong();
            this.DistrictID = in.readLong();
            this.Quantity = in.readInt();

        }

        public static final Parcelable.Creator<Cartbean> CREATOR = new Parcelable.Creator<Cartbean>() {
            @Override
            public Cartbean createFromParcel(Parcel source) {
                return new Cartbean(source);
            }

            @Override
            public Cartbean[] newArray(int size) {
                return new Cartbean[size];
            }
        };
    }
    /*非套餐*/
    public static class Cartbean01 implements Parcelable {
        long ProductId;

        public Cartbean01(long productId) {
            ProductId = productId;
        }

        public long getProductId() {
            return ProductId;
        }

        public void setProductId(long productId) {
            ProductId = productId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.ProductId);
        }

        protected Cartbean01(Parcel in) {
            this.ProductId = in.readLong();
        }

        public static final Parcelable.Creator<Cartbean01> CREATOR = new Parcelable.Creator<Cartbean01>() {
            @Override
            public Cartbean01 createFromParcel(Parcel source) {
                return new Cartbean01(source);
            }

            @Override
            public Cartbean01[] newArray(int size) {
                return new Cartbean01[size];
            }
        };
    }
    /*套餐*/
    public static class Cartbean02 implements Parcelable {
        long GroupSellID;

        public Cartbean02(long groupSellID) {
            GroupSellID = groupSellID;
        }

        public long getGroupSellID() {
            return GroupSellID;
        }

        public void setGroupSellID(long groupSellID) {
            GroupSellID = groupSellID;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.GroupSellID);
        }

        protected Cartbean02(Parcel in) {
            this.GroupSellID = in.readLong();
        }

        public static final Parcelable.Creator<Cartbean02> CREATOR = new Parcelable.Creator<Cartbean02>() {
            @Override
            public Cartbean02 createFromParcel(Parcel source) {
                return new Cartbean02(source);
            }

            @Override
            public Cartbean02[] newArray(int size) {
                return new Cartbean02[size];
            }
        };
    }


    public static class Cartbean03 implements Parcelable {
        long ShopId;
        long ProductId;
        long GroupSellID;

        public Cartbean03(long shopId, long productId, long groupSellID) {
            ShopId = shopId;
            ProductId = productId;
            GroupSellID = groupSellID;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.ShopId);
            dest.writeLong(this.ProductId);
            dest.writeLong(this.GroupSellID);
        }

        protected Cartbean03(Parcel in) {
            this.ShopId = in.readLong();
            this.ProductId = in.readLong();
            this.GroupSellID = in.readLong();
        }

        public static final Parcelable.Creator<Cartbean03> CREATOR = new Parcelable.Creator<Cartbean03>() {
            @Override
            public Cartbean03 createFromParcel(Parcel source) {
                return new Cartbean03(source);
            }

            @Override
            public Cartbean03[] newArray(int size) {
                return new Cartbean03[size];
            }
        };
    }

    public static class Cartbean04 implements Parcelable {
        public Cartbean04(long productId, long groupSellID) {
            ProductId = productId;
            GroupSellID = groupSellID;
        }

        long ProductId;
        long GroupSellID;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.ProductId);
            dest.writeLong(this.GroupSellID);
        }

        protected Cartbean04(Parcel in) {
            this.ProductId = in.readLong();
            this.GroupSellID = in.readLong();
        }

        public static final Parcelable.Creator<Cartbean04> CREATOR = new Parcelable.Creator<Cartbean04>() {
            @Override
            public Cartbean04 createFromParcel(Parcel source) {
                return new Cartbean04(source);
            }

            @Override
            public Cartbean04[] newArray(int size) {
                return new Cartbean04[size];
            }
        };
    }
}
