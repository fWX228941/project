package com.android.haobanyi.model.bean.home.location;

/**
 * Created by fWX228941 on 2016/5/19.
 *
 * @作者: 付敏
 * @创建日期：2016/05/19
 * @邮箱：466566941@qq.com
 * @当前文件描述：包括任何城市和城市列表在内的一个城市实体类，映射一个字段也是可以的
 */
public class CityBean {
    /*这个随着字段的增减也有所增减，变更*/
    //1.当前提供四个字段，id ,parent,name,type
    private int id;
    private int parent;
    private String name;
    private int type;
    public CityBean() {
        super();
    }
    //2.提供创建实体对象
    public CityBean(int id, int parent, String name) {
        super();
        this.id = id;
        this.parent = parent;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getParent() {
        return parent;
    }
    public void setParent(int parent) {
        this.parent = parent;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
