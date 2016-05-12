package com.example.wang.retrofittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wang.retrofittest.bean.MovieBean;
import com.example.wang.retrofittest.http.MovieService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mResult;
    private Button mSubmit;
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
        mSubmit = (Button) findViewById(R.id.btn_getinfo);
    }

    private void initOnClick() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovie();
            }
        });
    }

    private void getMovie() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService movieService = retrofit.create(MovieService.class);
        Call<MovieBean> call = movieService.getTopMovie(0, 10);
        call.enqueue(new Callback<MovieBean>() {
            @Override
            public void onResponse(Call<MovieBean> call, Response<MovieBean> response) {
                mResult.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<MovieBean> call, Throwable t) {
                mResult.setText(t.getMessage());
            }
        });
    }
}
