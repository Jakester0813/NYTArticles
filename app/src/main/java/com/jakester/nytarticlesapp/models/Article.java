package com.jakester.nytarticlesapp.models;

import com.google.gson.annotations.SerializedName;

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

    @SerializedName("uri")
    String mURI;

    @SerializedName("multimedia")
    Multimedia mMultimedia;

    public String getWebUrl(){
        return mWebUrl;
    }

    public String getSnippet(){
        return mSnippet;
    }

    public Headline getHeadline(){
        return mHeadline;
    }

    public String getURI(){
        return "http://" + mURI + ".jpg";
    }

    public Multimedia getMultimedia(){
        return mMultimedia;
    }
}
