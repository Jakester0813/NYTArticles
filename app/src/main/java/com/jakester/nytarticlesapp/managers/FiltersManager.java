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
    public boolean mArtChecked, mFashionChecked, mSportsChecked;
    public String mDate, mSortBy, mDateFilter;
    public int mSortPosition;

    public static FiltersManager getInstance(Context pContext){
        if(mInstance == null){
            mInstance = new FiltersManager(pContext);
        }
        return mInstance;
    }

    public FiltersManager(Context pContext){
        mPrefs = pContext.getSharedPreferences(NYTConstants.PREFS_NAME,Context.MODE_PRIVATE);
        mDate = mPrefs.getString(NYTConstants.DATE_PREFS,NYTConstants.SET_DATE);
        mSortBy = mPrefs.getString(NYTConstants.SORT_PREFS,null);
        mSortPosition = mPrefs.getInt(NYTConstants.SORT_POSITION_PREFS,0);
        mDateFilter = mPrefs.getString(NYTConstants.DATE_FILTER_PREFS,null);
        mArtChecked = mPrefs.getBoolean(NYTConstants.PREFS_ART,false);
        mFashionChecked = mPrefs.getBoolean(NYTConstants.PREFS_FASHION,false);
        mSportsChecked = mPrefs.getBoolean(NYTConstants.PREFS_SPORTS,false);

    }

    public String getDate(){
        return mDate;
    }

    public void setDate(String s){
        mDate = s;
        mPrefs.edit().putString(NYTConstants.DATE_PREFS, s).commit();
    }

    public String getFilterDate(){
        return mDateFilter;
    }

    public void setDateFilter(String s){
        mPrefs.edit().putString(NYTConstants.DATE_FILTER_PREFS, s).commit();
        mDateFilter = s;
    }

    public String getSortFilter(){
        return mSortBy;
    }

    public void setSortFilter(String s){
        if(!s.equals("select filter...")) {
            mPrefs.edit().putString(NYTConstants.SORT_PREFS, s).commit();
            mSortBy = s;
        }
    }

    public int getSortPosition(){
        return mSortPosition;
    }

    public void setSortPosition(int i){
        mPrefs.edit().putInt(NYTConstants.SORT_POSITION_PREFS, i).commit();
        mSortPosition = i;
    }

    public boolean getArt(){
        return mArtChecked;
    }

    public void setArtChecked(boolean value){
        mArtChecked = value;
        mPrefs.edit().putBoolean(NYTConstants.PREFS_ART, value).commit();
    }

    public boolean getFashion(){
        return mFashionChecked;
    }

    public void setFashionCheck(boolean value){
        mFashionChecked = value;
        mPrefs.edit().putBoolean(NYTConstants.PREFS_FASHION, value).commit();
    }

    public boolean getSports(){
        return mFashionChecked;
    }

    public void setSportCheck(boolean value){
        mSportsChecked = value;
        mPrefs.edit().putBoolean(NYTConstants.PREFS_SPORTS, value).commit();
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


}
