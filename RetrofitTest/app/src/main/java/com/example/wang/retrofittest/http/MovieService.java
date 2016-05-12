package com.example.wang.retrofittest.http;

import com.example.wang.retrofittest.bean.MovieBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by busr on 2016/5/12.
 */
public interface MovieService {

    @GET("top250")
    Call<MovieBean> getTopMovie(@Query("start")int start, @Query("count")int count);
}
