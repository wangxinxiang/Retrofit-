package com.example.wang.retrofittest.rxjava2;

import android.app.Activity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * Created by wang on 2017/8/3.
 */

public class TestActivity extends Activity {


    private void create() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                e.onNext("");
            }
        }).map(new Function<Object, String>() {
            @Override
            public String apply(@NonNull Object o) throws Exception {
                return null;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String o) throws Exception {

            }
        });
    }
}
