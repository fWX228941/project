

/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.*/


package com.android.haobanyi.api.net;
import java.util.Map;
import android.util.Log;

import com.android.haobanyi.api.Api;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class HttpEngine {
    private final static String TAG = "HttpEngine";
    //测试：private final static String SERVER_URL = "http://uat.b.quancome.com/platform/api";
    private final static String SERVER_URL = "http://192.168.0.110:57401";//其实这个URL就是一个方法口
    private final static String REQUEST_MOTHOD = "POST";
    private final static String ENCODE_TYPE = "UTF-8";
    private final static int TIME_OUT = 20000;

    private static HttpEngine instance = null;

    private HttpEngine() {

    }

    public static HttpEngine getInstance() {
        if (instance == null) {
            instance = new HttpEngine();
        }
        return instance;
    }

    public <T> T postHandle(Map<String, String> paramsMap, Type typeOfT) throws IOException {
        String data = joinParams(paramsMap);
        Log.d("fumin03",data);
        // 打印出请求
        Log.i(TAG, "request: " + data);

        HttpURLConnection connection = getConnection();
        Log.d("fumin005", connection.toString());
        connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
        connection.connect();
        Log.d("fumin04", connection.toString());
       // Log.d("fumin05", connection.getContent() + ""); 这个时候其实是没有内容的   问题就是出现在了这里
        Log.d("fumin08", connection+"");


        OutputStream os = connection.getOutputStream();
        Log.d("fumin06", os.toString());
        Log.d("fumin07", os + "");
        os.write(data.getBytes());
        Log.d("fumin11", data.getBytes() + "");
        os.flush();
        Log.d("fumin09", os.toString());
        Log.d("fumin10", os + "");
        Log.d("fumin008", connection.getResponseCode() + "");
       // connection.getResponseCode();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // 获取响应的输入流对象
            InputStream is = connection.getInputStream();
            Log.d("fumin12", is.toString());
            // 创建字节输出流对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 定义读取的长度
            int len = 0;
            // 定义缓冲区
            byte buffer[] = new byte[1024];
            // 按照缓冲区的大小，循环读取
            while ((len = is.read(buffer)) != -1) {
                // 根据读取的长度写入到os对象中
                Log.d("fumin13", is.read(buffer)+"");
                baos.write(buffer, 0, len);
            }
            // 释放资源
            is.close();
            baos.close();
            connection.disconnect();
            Log.d("fumin14", baos.toByteArray() + "");
            // 返回字符串,统统转化为string类型
            final String result = new String(baos.toByteArray());
            Log.d("fumin15", result);
            // 打印出结果  问题就是出现在了这里，崩溃的
            Log.i(TAG, "response: " + result);
            Gson gson = new Gson();

            //  这个地方有问题， 泛型，但是转化为typeOfT类时出现问题
            Log.d("fumin16", gson.fromJson(result,typeOfT).toString());
            Log.d("fumin17", gson.fromJson(result,typeOfT)+"");
            //return gson.fromJson(result, ApiResponse.class);
            //JSON数据和实体对象转化时出错导致的
            return gson.fromJson(result, typeOfT);//把json格式的字符串 转化为type类型  返回的成功验证码1和response event是怎么关联起来的困惑
        } else {
            connection.disconnect();
            return null;
        }
    }

    // 获取connection
    private HttpURLConnection getConnection() {
        HttpURLConnection connection = null;
        // 初始化connection
        try {
            // 根据地址创建URL对象
            // 先测试
            URL url = new URL(SERVER_URL+ Api.LOGIN);
            Log.d("fumin001", SERVER_URL);
            // 根据URL对象打开链接
            connection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            connection.setRequestMethod(REQUEST_MOTHOD);
            // 发送POST请求必须设置允许输入，默认为true
            connection.setDoInput(true);
            // 发送POST请求必须设置允许输出
            connection.setDoOutput(true);
            // 设置不使用缓存
            connection.setUseCaches(false);
            // 设置请求的超时时间
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Response-Type", "json");  //这些响应头的值是怎么来的？
            //connection.setChunkedStreamingMode(0);
            Log.d("fumin002", "002");
        } catch (IOException e) {
            Log.d("fumin003", "003");
            e.printStackTrace();
        }
        Log.d("fumin004", connection.toString()+"");
        return connection;
    }

    // 拼接参数列表   这个就是拼接服务器端具体的参数列表了
    private String joinParams(Map<String, String> paramsMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : paramsMap.keySet()) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            try {
                stringBuilder.append(URLEncoder.encode(paramsMap.get(key), ENCODE_TYPE));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuilder.append("&");
            Log.d("fumin01",stringBuilder.toString());
        }
        Log.d("fumin02",stringBuilder.substring(0, stringBuilder.length() - 1));
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
