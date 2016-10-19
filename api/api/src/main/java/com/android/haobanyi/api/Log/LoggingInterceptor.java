package com.android.haobanyi.api.Log;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by fWX228941 on 2016/5/12.
 *
 * @作者: 付敏
 * @创建日期：2016/05/12
 * @邮箱：466566941@qq.com
 * @当前文件描述：打印json格式的数据 【有点废弃了】
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        String requestLog = String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers());
        //YLog.d(String.format("Sending request %s on %s%n%s",
        //        request.url(), chain.connection(), request.headers()));
        if (request.method().compareToIgnoreCase("post") == 0) {
            requestLog = "\n" + requestLog + "\n" + bodyToString(request);
        }
        Log.d("fumin json", "request" + "\n" + requestLog);

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        String responseLog = String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers());

        String bodyString = response.body().string();

        Log.d("fumin json", "response" + "\n" + responseLog + "\n" + bodyString);

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), bodyString))
                .build();
        //return response;
    }


    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
