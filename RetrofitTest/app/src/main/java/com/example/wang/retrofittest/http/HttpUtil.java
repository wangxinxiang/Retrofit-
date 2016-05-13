package com.example.wang.retrofittest.http;

import com.example.wang.retrofittest.bean.HttpResult;
import com.example.wang.retrofittest.bean.Subject;
import com.example.wang.retrofittest.http.api.MovieService;
import com.example.wang.retrofittest.http.api.MovieService2;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by busr on 2016/5/13.
 * 封装网络请求
 * 初步只封装Retrofit的构造，
 */
public class HttpUtil {

    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit mRetrofit;

    private MovieService2 movieService2;

    private HttpUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(builder.build())
                .build();

        movieService2 = mRetrofit.create(MovieService2.class);
    }

    /**
     * 静态内部类单例模式
     */
    private static class SingleHolder{
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    /**
     * 利用静态内部类单例模式
     */
    public static HttpUtil getInstance() {
        return SingleHolder.INSTANCE;
    }

    public<T> void getTopMovie(Subscriber<T> subscriber, int start, int count) {
        movieService2.getTopMovie(start, count)
                .map(new HttpResultFun<T>())

    }

    private class HttpResultFun<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> tHttpResult) {
            if (tHttpResult.getCount() == 0) {
                throw new ApiException(100);
            }
            return tHttpResult.getSubjects();
        }
    }
}
