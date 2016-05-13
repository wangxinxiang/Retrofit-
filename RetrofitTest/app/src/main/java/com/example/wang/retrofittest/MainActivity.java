package com.example.wang.retrofittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wang.retrofittest.bean.HttpResult;
import com.example.wang.retrofittest.bean.Subject;
import com.example.wang.retrofittest.http.api.MovieService;
import com.example.wang.retrofittest.http.api.MovieService2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView mResult;
    private Button mSubmit1;
    private String baseUrl = "https://api.douban.com/v2/movie/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initOnClick();
    }

    private void initView() {
        mResult = (TextView) findViewById(R.id.tv_info);
        mSubmit1 = (Button) findViewById(R.id.btn_getinfo1);
    }

    private void initOnClick() {
        mSubmit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovie2();
            }
        });
    }

    /**
     * 只使用Retrofit进行网络请求
     */
    private void getMovie1() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService movieService = retrofit.create(MovieService.class);
        Call<HttpResult<List<Subject>>> call = movieService.getTopMovie(0, 10);
        call.enqueue(new Callback<HttpResult<List<Subject>>>() {
            @Override
            public void onResponse(Call<HttpResult<List<Subject>>> call, Response<HttpResult<List<Subject>>> response) {
                mResult.setText(response.body().getSubjects().toString());
            }

            @Override
            public void onFailure(Call<HttpResult<List<Subject>>> call, Throwable t) {
                mResult.setText(t.getMessage());
            }
        });

    }

    /**
     * 不封装，使用RXJava
     */
    private void getMovie2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        MovieService2 movieService2 = retrofit.create(MovieService2.class);

        movieService2.getTopMovie(0,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<List<Subject>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("getMovie2", e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult<List<Subject>> listHttpResult) {
                        mResult.setText(listHttpResult.getSubjects().toString());
                    }
                });

    }

}
