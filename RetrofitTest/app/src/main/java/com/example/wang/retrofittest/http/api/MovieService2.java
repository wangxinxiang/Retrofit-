package com.example.wang.retrofittest.http.api;

import com.example.wang.retrofittest.bean.HttpResult;
import com.example.wang.retrofittest.bean.Subject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Query;

/**
 * Created by busr on 2016/5/12.
 * 使用rxjava
 */
public interface MovieService2 {


//    @GET("top250")
    @HTTP(method = "GET", path = "top250")  //http可以配置使用其他网络请求方法
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);
}
