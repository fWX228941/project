package com.android.haobanyi.model.bean.shopping.conformorder;

import com.android.haobanyi.model.bean.shopping.ShoppingCartBean;

import java.util.List;

/**
 * Created by fWX228941 on 2016/6/30.
 *
 * @作者: 付敏
 * @创建日期：2016/06/30
 * @邮箱：466566941@qq.com
 * @当前文件描述：套餐和非套餐的其他服务
 * 最多两层嵌套，三层嵌套就复杂
 * 关联：ShopAttrList
 * 对应的接口：getShoppingCart
 * 对应的类：编辑，下拉服务匡ShoppingPageView
 * 每一个产品有一个服务列表
 * 这个bean 实际上是一个服务的集合，共享同一个GroupId 和同一个ProductId  ShopAttrListBean这个才是我想要的
 */
public class OtherServicesBean {
/*    private long ShopAttrID;
    private String AttrName;
    private double Price;*/

    private long GroupSellID = 0;//套餐ID  0就代表：非套餐
    private long ProductID;
    private List<ShopAttrListBean> ShopAttrList;

/*    public OtherServicesBean(long groupSellID,long productID,long shopAttrID, String attrName, double price)  {
        GroupSellID = groupSellID;
        ProductID = productID;
        ShopAttrID = shopAttrID;
        AttrName = attrName;
        Price = price;
    }*/

    public OtherServicesBean(long groupSellID,long productID,List<ShopAttrListBean> shopAttrList)  {
        GroupSellID = groupSellID;
        ProductID = productID;
        ShopAttrList= shopAttrList;
    }

    @Override
    public String toString() {
        String newStr = null;
        String oldStr = "";
        for (int i=0;i<ShopAttrList.size();i++){
            oldStr = "GroupSellID = "+GroupSellID+"; ProductID = "+ProductID+"; ShopAttrListBean 第("+i+")的值为 = "+"/n"+ShopAttrList.get(i).toString()+"/n"+"/n";
            newStr = oldStr+newStr;
        }
        return newStr;
    }

    public List<ShopAttrListBean> getShopAttrList() {
        return ShopAttrList;
    }

    public void setShopAttrList(List<ShopAttrListBean> ShopAttrList) {
        this.ShopAttrList = ShopAttrList;
    }
    /*
    * 说明：由套餐ID+产品ID+ShopAttrID   / 非套餐ID+产品ID+ShopAttrID 共同决定，显示位置
    * 显示内容：名字，总价=单价叠加 数量：由点击的次数来决定
    *
    * */

    //判断是否是套餐
    public boolean isGroupService(){
        return (GroupSellID==0)?false:true;
    }



    public long getGroupSellID() {
        return GroupSellID;
    }

    public void setGroupSellID(long groupSellID) {
        GroupSellID = groupSellID;
    }

    public long getProductID() {
        return ProductID;
    }

    public void setProductID(long productID) {
        ProductID = productID;
    }

    public static class ShopAttrListBean {  //选择其他服务，这个情况也需要考虑
        private long ShopAttrID;
        private String AttrName;
        private double Price;

        public ShopAttrListBean(long shopAttrID, String attrName, double price) {
            ShopAttrID = shopAttrID;
            AttrName = attrName;
            Price = price;
        }

        public long getShopAttrID() {
            return ShopAttrID;
        }

        public void setShopAttrID(long ShopAttrID) {
            this.ShopAttrID = ShopAttrID;
        }

        public String getAttrName() {
            return AttrName;
        }

        public void setAttrName(String AttrName) {
            this.AttrName = AttrName;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        @Override
        public String toString() {
            return "ShopAttrID = "+ShopAttrID+"; AttrName = "+AttrName+"; Price = "+Price;
        }
    }
}
