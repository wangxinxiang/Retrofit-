package com.example.wang.retrofittest.model;

/**
 * Created by busr on 2016/5/16.
 * 单例模式获取
 */
public class BaseModel {


    protected static class TopMovieHolder {
        private static final BaseModel INSTANCE = new BaseModel();
    }

    public static BaseModel getInstance () {return TopMovieHolder.INSTANCE;}



}
