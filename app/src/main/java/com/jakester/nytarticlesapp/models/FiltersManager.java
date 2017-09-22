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

    public String getFilterDate(){
        return mPrefs.getString(NYTConstants.DATE_FILTER_PREFS,null);
    }

    public void setDateFilter(String s){
        mPrefs.edit().putString(NYTConstants.DATE_FILTER_PREFS, s).commit();
    }

    public String getSortFilter(){
        return mPrefs.getString(NYTConstants.SORT_PREFS,null);
    }

    public void setSortFilter(String s){
        if(!s.equals("select filter...")) {
            mPrefs.edit().putString(NYTConstants.SORT_PREFS, s).commit();
        }
    }

    public int getSortPosition(){
        return mPrefs.getInt(NYTConstants.SORT_POSITION_PREFS,0);
    }

    public void setSortPosition(int i){
        mPrefs.edit().putInt(NYTConstants.SORT_POSITION_PREFS, i).commit();
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

    public String getNewsDeskFilter(){
        StringBuilder sb = new StringBuilder("");
        if(getArt() || getFashion() || getSports()) {
            sb.append("news_desk:(");
            if(getArt()){
                sb.append("\"Art\"");
            }
            if(getFashion()){
                sb.append("\"Fashion & Style\"");
            }
            if(getSports()){
                sb.append("\"Sports\"");
            }
            sb.append(")");
            return sb.toString();
        }
        return null;
    }

    public void setCheck(String key, boolean value){
        mPrefs.edit().putBoolean(key, value).commit();
    }


}
