package com.android.haobanyi.core.service;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基于NTP协议的网络时间
 */
public class NTPTime implements Runnable {

    private final List<String> sHosts;

    private volatile long mLocalTimeOffset;

    private static final String TAG = "NTPTime";

    private AsyncTask.Status mStatus = AsyncTask.Status.PENDING;

    {
        sHosts = new ArrayList<>(17);
        sHosts.add("ntp.sjtu.edu.cn");// 202.120.2.101 (上海交通大学网络中心NTP服务器地址）
        sHosts.add("s1a.time.edu.cn");// 北京邮电大学
        sHosts.add("s1b.time.edu.cn");// 清华大学
        sHosts.add("s1c.time.edu.cn");// 北京大学
        sHosts.add("s1d.time.edu.cn");// 东南大学
        sHosts.add("s1e.time.edu.cn");// 清华大学
        sHosts.add("s2a.time.edu.cn");// 清华大学
        sHosts.add("s2b.time.edu.cn");// 清华大学
        sHosts.add("s2c.time.edu.cn");// 北京邮电大学
        sHosts.add("s2d.time.edu.cn");// 西南地区网络中心
        sHosts.add("s2e.time.edu.cn");// 西北地区网络中心
        sHosts.add("s2f.time.edu.cn");// 东北地区网络中心
        sHosts.add("s2g.time.edu.cn");// 华东南地区网络中心
        sHosts.add("s2h.time.edu.cn");// 四川大学网络管理中心
        sHosts.add("s2j.time.edu.cn");// 大连理工大学网络中心
        sHosts.add("s2k.time.edu.cn");// CERNET桂林主节点
        sHosts.add("s2m.time.edu.cn");// 北京大学
        sHosts.add("1.cn.pool.ntp.org");
        sHosts.add("2.cn.pool.ntp.org");
        sHosts.add("3.cn.pool.ntp.org");
        sHosts.add("0.cn.pool.ntp.org");
        sHosts.add("cn.pool.ntp.org");
        sHosts.add("tw.pool.ntp.org");
        sHosts.add("0.tw.pool.ntp.org");
        sHosts.add("1.tw.pool.ntp.org");
        sHosts.add("2.tw.pool.ntp.org");
        sHosts.add("3.tw.pool.ntp.org");
    }

    private static class SingletonHolder {
        private static NTPTime SINGLETON = new NTPTime();

    }

    private NTPTime() {
        //no instance
    }

    public static NTPTime getInstance() {
        return SingletonHolder.SINGLETON;
    }

    public void updteNTPTime() {
        if (mStatus != AsyncTask.Status.RUNNING) {
            mStatus = AsyncTask.Status.RUNNING;
            new Thread(this).start();
        }
    }

    @Override
    public void run() {
        Collections.shuffle(sHosts);
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(5000);
        try {
            client.open();
            for (String host : sHosts) {
                boolean result = requestNTPTime(client, host);
                 if (result) break;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
        mStatus = AsyncTask.Status.FINISHED;
    }

    private boolean requestNTPTime(NTPUDPClient client, String host) {
        try {
            InetAddress hostAddr = InetAddress.getByName(host);
            TimeInfo info = client.getTime(hostAddr);
            info.computeDetails();
            mLocalTimeOffset = info.getOffset();
            Log.d(TAG, "ok  " + host + "  " + mLocalTimeOffset);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "failed  " + host);
        }
        return false;
    }

    public long getCurrentTime() {
        return mLocalTimeOffset + System.currentTimeMillis();
    }

}
