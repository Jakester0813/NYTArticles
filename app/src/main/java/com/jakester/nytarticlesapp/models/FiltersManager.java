package com.jakester.nytarticlesapp.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.jakester.nytarticlesapp.util.NYTConstants;

/**
 * Created by Jake on 9/20/2017.
 */

public class FiltersManager {

    public static FiltersManager mInstance;

    public SharedPreferences mPrefs;

    public static FiltersManager getInstance(Context pContext){
        if(mInstance == null){
            mInstance = new FiltersManager(pContext);
        }
        return mInstance;
    }

    public FiltersManager(Context pContext){
        mPrefs = pContext.getSharedPreferences(NYTConstants.PREFS_NAME,Context.MODE_PRIVATE);
    }

    public String getDate(){
        return mPrefs.getString(NYTConstants.DATE_PREFS,NYTConstants.SET_DATE);
    }

    public void setDate(String s){
        mPrefs.edit().putString(NYTConstants.DATE_PREFS, s).commit();
    }

    public boolean getArt(){
        return mPrefs.getBoolean(NYTConstants.PREFS_ART,false);
    }

    public boolean getFashion(){
        return mPrefs.getBoolean(NYTConstants.PREFS_FASHION,false);
    }

    public boolean getSports(){
        return mPrefs.getBoolean(NYTConstants.PREFS_SPORTS,false);
    }

    public void setCheck(String key, boolean value){
        mPrefs.edit().putBoolean(key, value).commit();
    }


}
