package com.saiyu.foreground.https;

import com.saiyu.foreground.consts.ConstValue;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiRetrofit {
    private static ApiRetrofit retrofit;

    private static ApiService mApiService;

    public ApiService getApiService() {
        return mApiService;
    }

    public static synchronized ApiRetrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new ApiRetrofit();
        }
        return retrofit;
    }

    private ApiRetrofit() {
        initRetrofit();
    }

    public static Retrofit initRetrofit() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor("OKHTTP");
        //设定日志级别
        httpLoggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        httpLoggingInterceptor.setColorLevel(Level.INFO);
        //添加拦截器
//        builder.addInterceptor(httpLoggingInterceptor);

        OkHttpClient apiClientClient = builder.connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new RequestHeaderInterceptor())
//                .addInterceptor(new RequestEncryptInterceptor())
                .build();

        Retrofit apiRetrofit = new Retrofit.Builder()
                .baseUrl(ConstValue.SERVR_URL)
                .addConverterFactory(SecretJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(apiClientClient)
                .build();

        mApiService = apiRetrofit.create(ApiService.class);
        return apiRetrofit;
    }

}
