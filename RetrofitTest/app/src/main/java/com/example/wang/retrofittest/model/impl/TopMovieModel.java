package com.example.wang.retrofittest.model.impl;

import com.example.wang.retrofittest.bean.HttpResult;
import com.example.wang.retrofittest.bean.Subject;
import com.example.wang.retrofittest.http.HttpUtil;
import com.example.wang.retrofittest.http.api.MovieService2;
import com.example.wang.retrofittest.model.ITopMovie;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by busr on 2016/5/16.
 * model层
 */
public class TopMovieModel implements ITopMovie {

    protected static class TopMovieHolder {
        private static final TopMovieModel INSTANCE = new TopMovieModel();
    }

    public static TopMovieModel getInstance () {return TopMovieHolder.INSTANCE;}

    @Override
    public void getTopMovie(Subscriber<List<Subject>> subscriber, int start, int count) {
        MovieService2 movieService2 = mHttpUtil.getMovieService2();
        Observable<HttpResult<List<Subject>>> observable = movieService2.getTopMovie(start, count);

        observable.map(new HttpUtil.HttpResultFun<List<Subject>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }
}
