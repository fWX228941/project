package com.android.haobanyi.model.bean.shopping;

/**
 * Created by fWX228941 on 2016/6/6.
 *
 * @作者: 付敏
 * @创建日期：2016/06/06
 * @邮箱：466566941@qq.com
 * @当前文件描述：从获取购物车列表获取的数据过滤出父列表的数据
 * 亮点：在构造函数的参数中除了通常意义的店名，id 外 因为存在一个多选框【isChecked 是否勾选】和一个编辑选项【isEditing】 另外领券可能还会添加一个状态以及商店logo字段*/


public class ShoppingParentItemBean {
//* 店铺ID

    private String id;
//* 店铺名称

    private String name;
    //private String shopLogoUrl;


    private boolean isChecked = false;  // 默认的未勾选状态
    private boolean isEditing = false;//默认都是未编辑，未选择
    //领券可能还会添加一个状态以及商店logo字段

    public ShoppingParentItemBean(String id, String name, boolean isChecked, boolean isEditing) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
        this.isEditing = isEditing;
    }

    public ShoppingParentItemBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
