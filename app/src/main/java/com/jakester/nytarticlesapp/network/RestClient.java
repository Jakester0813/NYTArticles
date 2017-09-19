package com.jakester.nytarticlesapp.network;

import com.jakester.nytarticlesapp.util.NYTConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jake on 9/18/2017.
 */

public class RestClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(NYTConstants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        return retrofit;
    }

}
