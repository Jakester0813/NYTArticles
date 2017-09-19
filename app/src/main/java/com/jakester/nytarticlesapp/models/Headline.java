package com.jakester.nytarticlesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jake on 9/18/2017.
 */

public class Headline {
    @SerializedName("main")
    String mMain;

    @SerializedName("print_headline")
    String mPrintHeadline;

    public String getMain(){
        return mMain;
    }

    public String getPrintHeadline(){
        return mPrintHeadline;
    }
}
