package com.jakester.nytarticlesapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jake on 9/18/2017.
 */

public class Article {

    @SerializedName("web_url")
    String mWebUrl;

    @SerializedName("snippet")
    String mSnippet;

    @SerializedName("headline")
    Headline mHeadline;

    @SerializedName("multimedia")
    List<Image> mImages;

    public String getWebUrl() { return mWebUrl; }

    public String getSnippet(){
        return mSnippet;
    }

    public Headline getHeadline(){
        return mHeadline;
    }

    public List<Image> getImages(){
        return mImages;
    }
}
