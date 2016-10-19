package com.android.haobanyi.listener;

import android.Manifest;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.android.haobanyi.activity.guid.register.RegisterActivity02;
import com.android.haobanyi.activity.guid.register.ThirdLoginActivity;
import com.android.haobanyi.util.ToastUtil;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fumin on 16/3/15.
 * 获取验证码：
 * 参考文档：http://www.jianshu.com/p/01b36e41d507
 * http://www.jianshu.com/p/a436c81278e4
 * http://blog.csdn.net/kaloda2011/article/details/21032829
 * http://www.cnblogs.com/jiayaguang/p/4366384.html
 *
 *
 *
 */
public class SmsObserver extends ContentObserver{

    private Context mContext;
    private Handler mHandler;
    private static final int MSG_RECEIVED_CODE = 1;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

        Log.d("main", "SMS has changed!");
        Log.d("main", uri.toString());
        // 短信内容变化时，第一次调用该方法时短信内容并没有写入到数据库中,return
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }
        Acp.getInstance(mContext).request(new AcpOptions.Builder().setPermissions(Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        getValidateCode();//获取短信验证码

                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastUtil.showShort(permissions.toString() + "权限拒绝");

                    }
                });

    }

    /**
     * 获取短信验证码
     */
    private void getValidateCode() {
        String code = "";
        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String address = c.getString(c.getColumnIndex("address"));
                String body = c.getString(c.getColumnIndex("body"));

                //13162364720为发件人的手机号码    发件认得号码不固定吗？1069086120764 10690447720764
                /**/
                Pattern pattern01 = Pattern.compile("1069\\d+");
                Matcher matcher02 = pattern01.matcher(address);
                if (!matcher02.matches()) {
                    return;
                }
                Log.d("main", "发件人为:" + address + " ," + "短信内容为:" + body);

                Pattern pattern = Pattern.compile("(\\d{6})");
                Matcher matcher = pattern.matcher(body);

                if (matcher.find()) {
                    code = matcher.group(0);
                    Log.d("main", "验证码为: " + code);
                    mHandler.obtainMessage(MSG_RECEIVED_CODE, code).sendToTarget();
                }

            }
            c.close();
        }
    }
}
