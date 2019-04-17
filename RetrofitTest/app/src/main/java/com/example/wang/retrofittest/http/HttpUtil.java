package com.example.wang.retrofittest.http;

import com.example.wang.retrofittest.bean.HttpResult;
import com.example.wang.retrofittest.bean.Subject;
import com.example.wang.retrofittest.http.api.MovieService;
import com.example.wang.retrofittest.http.api.MovieService2;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by busr on 2016/5/13.
 * 封装网络请求
 * 初步只封装Retrofit的构造，
 */
public class HttpUtil {

    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit mRetrofit;

    private HttpUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())        //retrofit2默认使用OkHttp，不需要这部设置了
                .build();
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

//    public<T> void getTopMovie(Subscriber<T> subscriber, int start, int count) {
//        movieService2.getTopMovie(start, count)
//                .map(new HttpResultFun<T>())
//
//    }

    /**
     * 对返回结果做通用性的判断，并返回所指定的body。
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     * @param <T>
     */
    public static class HttpResultFun<T> implements Function<HttpResult<T>, T> {

        @Override
        public T apply(HttpResult<T> tHttpResult) throws Exception {
            if (tHttpResult.getCount() == 0) {
                throw new ApiException(100);
            }
            return tHttpResult.getSubjects();
        }
    }

    public <T> T createApi(Class<T> service){
        return mRetrofit.create(service);
    }
}
