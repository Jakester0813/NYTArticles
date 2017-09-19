package com.jakester.nytarticlesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jake on 9/18/2017.
 */

public class Response {

    @SerializedName("response")
    ArticlesResponse mArticlesResponse;

    public ArticlesResponse getArticlesResponse(){
        return mArticlesResponse;
    }
}
