package com.example.wang.retrofittest.http.api;

import com.example.wang.retrofittest.bean.HttpResult;
import com.example.wang.retrofittest.bean.Subject;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by busr on 2016/5/12.
 * 使用rxjava
 */
public interface MovieService2 {

    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);
}
