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
    public String mSnippet;

    @SerializedName("headline")
    public Headline mHeadline;

    @SerializedName("multimedia")
    public List<Image> mImages;

    public String imageUrl;

    public String getWebUrl() { return mWebUrl; }

    public String getSnippet(){
        return mSnippet;
    }

    public Headline getHeadline(){
        return mHeadline;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(){
        imageUrl = mImages.get(0).mGetUrl();
    }
}
