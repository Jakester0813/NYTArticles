package com.jakester.nytarticlesapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jakester.nytarticlesapp.R;
import com.jakester.nytarticlesapp.interfaces.NYTArticlesService;
import com.jakester.nytarticlesapp.models.Response;
import com.jakester.nytarticlesapp.network.RestClient;
import com.jakester.nytarticlesapp.util.APIUtility;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getArticles();
    }

    public void getArticles(){
        NYTArticlesService service = APIUtility.getArticleService();
        service.getArticles().enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d("","");
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("","");
            }
        });
    }
}
