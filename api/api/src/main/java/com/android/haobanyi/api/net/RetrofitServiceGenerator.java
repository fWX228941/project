package com.android.haobanyi.api.net;

import android.util.Log;

import com.android.haobanyi.api.Log.LoggingInterceptor;
import com.android.haobanyi.api.data.RRSTClinetApi;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;


/**
 * Created by fWX228941 on 2016/4/20.
 *
 * @作者: 付敏
 * @创建日期：2016/04/20
 * @邮箱：466566941@qq.com
 * @当前文件描述：Retrofit 框架性质的网络服务引擎  第二版本  向core层提供接口类
 */
public class RetrofitServiceGenerator {
    /*
    *     private final static String TAG = "HttpEngine";
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


    *
    * 先本地，后外网
    * */
    //1.域名地址
    private static final String BASE_SERVER_URL = "http://192.168.0.10:8090/";  //登录接口可用  boss的接口
    private static final String BASE_SERVER_URL_03 = "http://192.168.0.88:8089/"; //测试服务器  廖敏的http://192.168.0.2:8090  先这个

    private static final String BASE_SERVER_URL_01 ="http://192.168.0.2:8090/";//http://www.haobanyi.com/
    private static final String BASE_SERVER_URL_02 ="http://www.haobanyi.com/";  //haobhanyi 登录注册请求失效了
    //2.得到满足RESTapi规范的服务器接口,返回给客户端
    public static RRSTClinetApi api ;
    //3.时间戳
    public String date ;

    public static RRSTClinetApi getApi(){
        if (null == api) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

/*            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder().header("authorization","x")
                            .method(original.method(),original.body()).build();
                    return chain.proceed(request);
                }
            });*/
//            //这个地方需要添加log 同时添加请求头 参考文档：https://futurestud.io/blog/retrofit-2-manage-request-headers-in-okhttp-interceptor
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Headers headers = chain.request().headers();
                            Log.d("RetrofitServiceGenerato", headers.toString());//这些headers为什么只包括自定义的属性而不包括其他的一些属性
/*                            String date = headers.get("Date");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
                            String timeStamp = null;
                            try {
                                Date serverDateUAT = sdf.parse(date);
                                timeStamp = Long.toString(serverDateUAT.getTime()/1000L);
                                Log.d("fumin02",timeStamp);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }*/
                            Request request = chain.request().newBuilder()
                                    .build();
                            return chain.proceed(request); // 这个地方有问
                        }
                    })
                    .build();

            Retrofit restrofit = new Retrofit.Builder() // .client(client)
                    .client(client)
                    .baseUrl(BASE_SERVER_URL_02)
                    .addConverterFactory(GsonConverterFactory.create()).build();


            //Create an implementation of the API endpoints defined by the {@code service} interface.
            api= restrofit.create(RRSTClinetApi.class);
        }

        return api;

    }


/*    public static RRSTClinetApi getApi(Object value){
        if (null == api) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

*//*            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder().header("authorization","x")
                            .method(original.method(),original.body()).build();
                    return chain.proceed(request);
                }
            });*//*
//            //这个地方需要添加log 同时添加请求头 参考文档：https://futurestud.io/blog/retrofit-2-manage-request-headers-in-okhttp-interceptor
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Headers headers = chain.request().headers();
                            Log.d("RetrofitServiceGenerato", headers.toString());//这些headers为什么只包括自定义的属性而不包括其他的一些属性
*//*                            String date = headers.get("Date");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
                            String timeStamp = null;
                            try {
                                Date serverDateUAT = sdf.parse(date);
                                timeStamp = Long.toString(serverDateUAT.getTime()/1000L);
                                Log.d("fumin02",timeStamp);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }*//*
                            Request request = chain.request().newBuilder()
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            Retrofit restrofit = new Retrofit.Builder() // .client(client)
                    .client(client)
                    .baseUrl(BASE_SERVER_URL_02)
                    .addConverterFactory(new Converter.Factory() {
                        @Override
                        public Converter<String, RequestBody> requestBodyConverter(Type type, Annotation[]
                                parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
                            return new Converter<String, RequestBody>() {
                                @Override
                                public RequestBody convert(String value) throws IOException {
                                    return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), value);
                                }
                            };
                        }

                        @Override
                        public Converter<ResponseBody, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                            return new Converter<ResponseBody, String>() {
                                @Override
                                public String convert(ResponseBody value) throws IOException {
                                    return value.string();
                                }
                            };
                        }
                    }).build();


            //Create an implementation of the API endpoints defined by the {@code service} interface.
            api= restrofit.create(RRSTClinetApi.class);
        }

        return api;

    }*/
}
