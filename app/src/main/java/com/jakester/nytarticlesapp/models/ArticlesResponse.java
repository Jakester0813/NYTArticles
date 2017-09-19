package com.jakester.nytarticlesapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jake on 9/18/2017.
 */

public class ArticlesResponse {

    @SerializedName("docs")
    List<Article> mArticles;

    public List<Article> getArticles(){
        return mArticles;
    }
}
