package com.example.wang.retrofittest.model;

import com.example.wang.retrofittest.bean.Subject;
import com.example.wang.retrofittest.http.HttpUtil;

import java.util.List;
import rx.Subscriber;

/**
 * Created by busr on 2016/5/16.
 * 定义接口model
 */
public interface ITopMovie {

    HttpUtil mHttpUtil = HttpUtil.getInstance();

    /**
     * 定义获取豆瓣电影排行的接口
     * @param start 开始位置
     * @param count 数量
     */
    void getTopMovie(Subscriber<List<Subject>> subscriber, int start, int count);
}
