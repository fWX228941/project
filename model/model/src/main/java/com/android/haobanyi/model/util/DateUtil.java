package com.android.haobanyi.model.util;

import android.provider.ContactsContract;
import android.text.format.Time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by fWX228941 on 2016/4/28.
 *
 * @作者: 付敏
 * @创建日期：2016/04/28
 * @邮箱：466566941@qq.com
 * @当前文件描述：日期操作工具类，参考文档：http://blog.csdn.net/xuduzhoud/article/details/27526177
 * 一个坑：Date、Calendar，他们在没有显示设置其时区时，取到的当前时间均为系统默认时区的时间，即使给定一个时间，同样是按系统默认时区来换算时间，所以说他们都是与时区相关的。
 * 补坑方案：http://www.2cto.com/kf/201312/266908.html
 * setTimeZone
 *     * 在线校验：http://coderschool.cn/tool/index.php/Index/time
 * http://www.kjson.com/time/
 *
 * 参考文档：
 * http://my.oschina.net/OpenSourceBO/blog/511683
 * http://josh-persistence.iteye.com/blog/2230074
 *
 * Android时间校验真是恶心死我了
 *
 * 这里有一个大坑就是时间校验，会出现时准时不准的情况
 * 时区方案参考设计：https://github.com/Ryfthink/Android-UTC-8
 *Android客户端转换php服务端获取的时间戳的转换  [更多时间校验]
 * http://blog.csdn.net/jiangwei0910410003/article/details/17412671
 *
 * 时间制之间的转化
 */
public class DateUtil {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";//默认就是到秒
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;

    public static Date str2Date(String str) {
        return str2Date(str, null);
    }

    /**
     * string -> date 加上了时区
     * 对于已经设定为GMT时间标准的dataFormat来说，一切需要他转换的字符串日期都是GMT标准时间，转换后返回的date由于默认准守系统默认时区
     * 所以需要转换给data的默认日期需要+8
     */
    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            date = sdf.parse(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;

    }

    public static Calendar str2Calendar(String str) {
        return str2Calendar(str, null);

    }

    public static Calendar str2Calendar(String str, String format) {

        Date date = str2Date(str, format);
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c;

    }

    public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
        return date2Str(c, null);
    }

    public static String date2Str(Calendar c, String format) {
        if (c == null) {
            return null;
        }
        return date2Str(c.getTime(), format);
    }

    public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
        return date2Str(d, null);
    }

    public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }
    public static String date2Str(Long d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }
    public static String getCurDateStr() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
                + c.get(Calendar.DAY_OF_MONTH) + "-"
                + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
                + ":" + c.get(Calendar.SECOND);
    }

    /**
     * 获得当前日期的字符串格式 获取时间戳
     */
    public static String getCurDateStr(String format) {
        Calendar c = Calendar.getInstance();
        return date2Str(c, format);
    }
    /*
    *  获取当前的时间戳， 当前时间距离1970年1月1号的秒数数时间差   unix 的时间戳
    *   方式有三：http://tangmingjie2009.iteye.com/blog/1543166
    *   System.currentTimeMillis();  效率最高
    *   Calendar.getInstance().getTimeInMillis(); 最差
    *   new Date().getTime();  其次
    *http://blog.csdn.net/scyatcs/article/details/39081689\
    * 在线校验：http://coderschool.cn/tool/index.php/Index/time
    * http://www.kjson.com/time/
    * */
    public static String getTimeStamp() {
        Calendar calendar =  Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return Long.toString(calendar.getTimeInMillis()/1000L);// 这个是转化为秒
    }
    public static String getTimeStamp01() {
        Time shanghai = new Time("Asia/Shanghai");
        shanghai.set(System.currentTimeMillis());
        String ti = shanghai.format2445();
        return ti;
    }
    public static String getTimeStamp02() {
        Calendar calendar =  Calendar.getInstance();
        //calendar.getTimeInMillis();GMT 设置整个也是没有问题的 我这里坑也比他多 刚好时区相差八个小时
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        return Long.toString(calendar.getTimeInMillis()/1000L);
    }

    /**
     * 格式到秒
     *
     * @return time -> yyyy-MM-dd-HH-mm-ss
     */
    public static String getMillon(long time) {

        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);

    }

    /**
     * 格式到天
     *
     * @return time -> yyyy-MM-dd
     */
    public static String getDay(long time) {

        return new SimpleDateFormat("yyyy-MM-dd").format(time);

    }

    /**
     * 格式到毫秒
     *
     * @return time -> yyyy-MM-dd-HH-mm-ss-SSS
     */
    public static String getSMillon(long time) {

        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);

    }



















    /*获取UTC时间，参考文档http://wyong.blog.51cto.com/1115465/1617679*/
    /*
    * 修改时区：http://www.2cto.com/kf/201312/266908.html
    *
    * */


    public static long getGMTUnixTimeByCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        // 获取当前时区下日期时间对应的时间戳
        long unixTime = calendar.getTimeInMillis();
        // 获取标准格林尼治时间下日期时间对应的时间戳
        long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();
        return unixTimeGMT;
    }

}
