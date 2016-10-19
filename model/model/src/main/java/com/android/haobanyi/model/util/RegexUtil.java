package com.android.haobanyi.model.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fWX228941 on 2016/4/28.
 *
 * @作者: 付敏
 * @创建日期：2016/04/28
 * @邮箱：466566941@qq.com
 * @当前文件描述：合法性检查工具类，通常利用正则表达式对输入的字符串进行合法性检查
 * 应用参加：手机号码/密码/邮箱 格式检查
 * 更多参考：http://www.oschina.net/code/snippet_1021818_47946
 *http://www.open-open.com/code/view/1434556625708
 * 校验邮箱、手机号和身份证等
 */
public class RegexUtil {
    /*手机格式Pattern.compile("[1][34578]\\d{9}")*/
    private final static Pattern mobilePattern = Pattern.compile("1\\d{10}");
    /*邮箱格式：这个值得商榷*/
    private final static Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    /*判断是否是合法的手机号码*/
    public static boolean validateMobile(String mobiles) {
        Matcher m = mobilePattern.matcher(mobiles);
        return m.matches();
    }

    // 判断email格式是否正确 匹配了就是合法
    public static boolean validateEmail(String email) {
        Matcher m = emailPattern.matcher(email);
        return m.matches();
    }
    //判断是否是合法的密码长度，默认至少五位，可以制定更负责的规则  这个涉及到实时监听用户的输入
    /*
    * 关于密码两层过滤：
    * 第一次：去掉空格符
    * 第二次：检查位数，或者实时检查位数也是可以的
    *
    * */
    public static boolean validatePassword(String password) {
        if (password.length() > 5 && password.length()<17 ){
            return true;
        }else {
            return false;
        }

    }
}
