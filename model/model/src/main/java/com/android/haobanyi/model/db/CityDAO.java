package com.android.haobanyi.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.haobanyi.model.bean.home.location.CityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fWX228941 on 2016/5/19.
 *
 * @作者: 付敏
 * @创建日期：2016/05/19
 * @邮箱：466566941@qq.com
 * @当前文件描述：对城市数据库增删改查的操作
 */
public class CityDAO {
    /*虚设，代表没有好使用*/
    public static List<CityBean> getProvencesOrCityOnId(int id){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(CityDBManager.databasePath + "/" + CityDBManager.DB_NAME, null);
        List<CityBean> cityBeans = new ArrayList<CityBean>();//String.valueOf(type)
        Cursor cursor = db.rawQuery("select * from REGIONS where _id="+id,null);
        while (cursor.moveToNext()) {
            CityBean cityBean = new CityBean();
            int _id = cursor.getInt(cursor
                    .getColumnIndex("_id"));
            int parent = cursor.getInt(cursor
                    .getColumnIndex("parent"));
            String name = cursor.getString(cursor
                    .getColumnIndex("name"));
            int type1 = cursor.getInt(cursor
                    .getColumnIndex("type"));
            cityBean.setId(_id);
            cityBean.setParent(parent);
            cityBean.setName(name);
            cityBean.setType(type1);
            cityBeans.add(cityBean);
        }
        cursor.close();
        db.close();
        return cityBeans;
    }
    public static List<CityBean> getProvencesOrCity(int type){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(CityDBManager.databasePath + "/" + CityDBManager.DB_NAME, null);
        List<CityBean> cityBeans = new ArrayList<CityBean>();//String.valueOf(type)
        Cursor cursor = db.rawQuery("select * from REGIONS where type="+type,null);
        while (cursor.moveToNext()) {
            CityBean cityBean = new CityBean();
            int _id = cursor.getInt(cursor
                    .getColumnIndex("_id"));
            int parent = cursor.getInt(cursor
                    .getColumnIndex("parent"));
            String name = cursor.getString(cursor
                    .getColumnIndex("name"));
            int type1 = cursor.getInt(cursor
                    .getColumnIndex("type"));
            cityBean.setId(_id);
            cityBean.setParent(parent);
            cityBean.setName(name);
            cityBean.setType(type1);
            cityBeans.add(cityBean);
        }
        cursor.close();
        db.close();
        return cityBeans;
    }

    public static List<CityBean> getProvencesOrCityOnParent(int parent){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(CityDBManager.databasePath + "/" + CityDBManager.DB_NAME, null);
        List<CityBean> cityBeans = new ArrayList<CityBean>();//String.valueOf(type)
        Cursor cursor = db.rawQuery("select * from REGIONS where parent="+parent,null);
        while (cursor.moveToNext()) {
            CityBean cityBean = new CityBean();
            int _id = cursor.getInt(cursor
                    .getColumnIndex("_id"));
            int parent1 = cursor.getInt(cursor
                    .getColumnIndex("parent"));
            String name = cursor.getString(cursor
                    .getColumnIndex("name"));
            int type = cursor.getInt(cursor
                    .getColumnIndex("type"));
            cityBean.setId(_id);
            cityBean.setParent(parent1);
            cityBean.setName(name);
            cityBean.setType(type);
            cityBeans.add(cityBean);
        }
        cursor.close();
        db.close();
        return cityBeans;
    }


    //插入  ，不用
    public static void insertRegion(SQLiteDatabase db, CityBean ri) {
        ContentValues values = new ContentValues();
        values.put("parent", ri.getParent());
        values.put("name", ri.getName());
        values.put("type", ri.getType());
        db.insert("REGIONS", null, values);
    }

    //返回所有的省市信息
    public static List<CityBean> queryAllInfo() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(CityDBManager.databasePath + "/" + CityDBManager.DB_NAME, null);
        List<CityBean> cityBeans = new ArrayList<CityBean>();
        Cursor cursor = db.rawQuery("select * from REGIONS", null);
        while (cursor.moveToNext()) {
            CityBean cityBean = new CityBean();
            int _id = cursor.getInt(cursor
                    .getColumnIndex("_id"));
            int parent = cursor.getInt(cursor
                    .getColumnIndex("parent"));
            String name = cursor.getString(cursor
                    .getColumnIndex("name"));
            int type = cursor.getInt(cursor
                    .getColumnIndex("type"));
            cityBean.setId(_id);
            cityBean.setParent(parent);
            cityBean.setName(name);
            cityBean.setType(type);


			/*kcontent = cursor.getString(cursor
					.getColumnIndex("remarkcontent"));
			String issettingremind = cursor.getString(cursor
					.getColumnIndex("issettingremind"));
			String isdaoqi = cursor.getString(cursor.getColumnIndex("isdaoqi"));
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			remind.set_id(_id);

			Integer netid = cursor.getInt(cursor.getColumnIndex("netid"));
			remind.setNetid(netid);

			Integer starcount = cursor.getInt(cursor
					.getColumnIndex("starcount"));
			remind.setStarcount(starcount);

			long createtime = cursor*//*.getLong(cursor
					.getColumnIndex("createtime"));
			remind.setCreatetime(createtime);

			String remind_time_format = cursor.getString(cursor
					.getColumnIndex("remind_time_format"));
			remind.setRemind_time_format(remind_time_format);*/

			/*remind.setCustomerName(customerName);
			remind.setIszhuanshu(iszhuanshu);
			remind.setProductName(productName);
			remind.setProductStyle(productStyle);
			remind.setLastfournumber(lastfournumber);
			remind.setBuymoney(buymoney);
			remind.setBuydate(buydate);
			remind.setDaoqidate(daoqidate);
			remind.setDaoqi_remind_time(daoqi_remind_time);
			remind.setRepeat_remind_style(repeat_remind_style);
			remind.setRemarkcontent(remarkcontent);
			remind.setIssettingremind(issettingremind);
			remind.setIsdaoqi(isdaoqi);*/


            cityBeans.add(cityBean);
        }
        cursor.close();
        db.close();
        return cityBeans;
    }

    public static CityBean querySingleRemind(SQLiteDatabase db, int _id) {
        String sql = "select * from remindtable where _id =" + _id;
        Cursor cursor = db.rawQuery(sql, null);
        CityBean remind = null;
        if (cursor.moveToNext()) {


			/*
			remind = new TimeRemind();
			String customerName = cursor.getString(cursor
					.getColumnIndex("customerName"));
			String iszhuanshu = cursor.getString(cursor
					.getColumnIndex("iszhuanshu"));
			String productName = cursor.getString(cursor
					.getColumnIndex("productName"));
			String productStyle = cursor.getString(cursor
					.getColumnIndex("productStyle"));
			String lastfournumber = cursor.getString(cursor
					.getColumnIndex("lastfournumber"));
			String buymoney = cursor.getString(cursor
					.getColumnIndex("buymoney"));
			String buydate = cursor.getString(cursor.getColumnIndex("buydate"));
			String daoqidate = cursor.getString(cursor
					.getColumnIndex("daoqidate"));
			String daoqi_remind_time = cursor.getString(cursor
					.getColumnIndex("daoqi_remind_time"));
			String repeat_remind_style = cursor.getString(cursor
					.getColumnIndex("repeat_remind_style"));
			String remarkcontent = cursor.getString(cursor
					.getColumnIndex("remarkcontent"));
			String issettingremind = cursor.getString(cursor
					.getColumnIndex("issettingremind"));
			String isdaoqi = cursor.getString(cursor.getColumnIndex("isdaoqi"));
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			remind.set_id(id);

			Integer netid = cursor.getInt(cursor.getColumnIndex("netid"));
			remind.setNetid(netid);

			Integer starcount = cursor.getInt(cursor
					.getColumnIndex("starcount"));
			remind.setStarcount(starcount);

			long createtime = cursor.getLong(cursor
					.getColumnIndex("createtime"));
			remind.setCreatetime(createtime);

			String remind_time_format = cursor.getString(cursor
					.getColumnIndex("remind_time_format"));
			remind.setRemind_time_format(remind_time_format);

			remind.setCustomerName(customerName);
			remind.setIszhuanshu(iszhuanshu);
			remind.setProductName(productName);
			remind.setProductStyle(productStyle);
			remind.setLastfournumber(lastfournumber);
			remind.setBuymoney(buymoney);
			remind.setBuydate(buydate);
			remind.setDaoqidate(daoqidate);
			remind.setDaoqi_remind_time(daoqi_remind_time);
			remind.setRepeat_remind_style(repeat_remind_style);
			remind.setRemarkcontent(remarkcontent);
			remind.setIssettingremind(issettingremind);
			remind.setIsdaoqi(isdaoqi);

		*/}
        cursor.close();
        return remind;
    }

    public static void deleteRegion(int _id, SQLiteDatabase db) {
        db.execSQL("delete from remindtable where _id = ?",
                new Object[] { _id });
    }

}
