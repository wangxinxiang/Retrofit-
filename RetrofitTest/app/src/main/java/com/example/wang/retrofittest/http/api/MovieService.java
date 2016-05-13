package com.example.wang.retrofittest.http.api;

import com.example.wang.retrofittest.bean.HttpResult;
import com.example.wang.retrofittest.bean.Subject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by busr on 2016/5/12.
 * 只使用retrofit
 */
public interface MovieService {

    @GET("top250")
    Call<HttpResult<List<Subject>>> getTopMovie(@Query("start")int start, @Query("count")int count);
}
