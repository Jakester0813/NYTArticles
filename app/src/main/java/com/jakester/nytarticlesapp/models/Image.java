package com.jakester.nytarticlesapp.models;

import com.google.gson.annotations.SerializedName;
import com.jakester.nytarticlesapp.util.NYTConstants;

/**
 * Created by Jake on 9/19/2017.
 */

public class Image {
    @SerializedName("url")
    String url;

    public String mGetUrl(){
        return NYTConstants.NYT_URL + url;
    }
}
