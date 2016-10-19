package com.android.haobanyi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by fWX228941 on 2016/4/20.
 *
 * @作者: 付敏
 * @创建日期：2016/04/20
 * @邮箱：466566941@qq.com
 * @当前文件描述：网络相关工具类,更多内容参考：http://www.cnblogs.com/codeworker/archive/2012/04/23/2467180.html
 */
public class NetWorkUtil {
    //1.检查网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    //2.利用WiFi获取当前手机IP地址
    public static String getIpAddressWithWifi(Context context){

        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int IpAddress = wifiInfo.getIpAddress();
            StringBuffer formattedIpAddress = new StringBuffer();
            formattedIpAddress.append(IpAddress & 0xFF).append(".");
            formattedIpAddress.append((IpAddress >> 8) & 0xFF).append(".");
            formattedIpAddress.append((IpAddress >> 16) & 0xFF).append(".");
            formattedIpAddress.append((IpAddress >> 24) & 0xFF);
            Log.d("fumin", "ip:" + formattedIpAddress.toString());
            return formattedIpAddress.toString();
        } catch (Exception ex) {
            return " 获取IP出失败，请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
        }

    }
    //3.利用GPRS流量获取当前手机IP地址
    public static String getIpAddressWithGPRS() {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    //4.判断WiFi是否打开
    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
