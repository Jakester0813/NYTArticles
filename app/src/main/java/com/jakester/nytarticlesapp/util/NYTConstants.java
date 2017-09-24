package com.jakester.nytarticlesapp.util;

/**
 * Created by Jake on 9/18/2017.
 */

public class NYTConstants {

    public static final String URL = "https://api.nytimes.com/";
    public static final String EXTENDED_URL = "svc/search/v2/articlesearch.json?api-key=b142e76bf1f24179af9d13cbf2403258";
    public static final String NYT_URL = "http://www.nytimes.com/";

    public static final String SET_DATE = "Set Begin Date";

    public static final String PREFS_NAME = "NYTimesPrefs";
    public static final String DATE_PREFS = "begin_date";
    public static final String DATE_FILTER_PREFS = "begin_date_filter";
    public static final String SORT_POSITION_PREFS = "sort_position";
    public static final String SORT_PREFS = "sort";
    public static final String PREFS_ART = "art";
    public static final String PREFS_DINING = "dining";
    public static final String PREFS_FASHION = "fashion";
    public static final String PREFS_HOME = "home";
    public static final String PREFS_MOVIES = "movies";
    public static final String PREFS_SPORTS = "sports";

    public static final String EMPTY_STRING = "";
    public static final String ZERO = "0";
    public static final String SLASH = "/";

    public static final String TOPIC_NYTIMES = "topics.nytimes.com";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String UTF8 = "UTF-8";

    public static final String SHARE_ARTICLE = "Share Article";
    public static final String PROGRESS_DIALOG_MESSAGE = "Loading articles. Please wait...";

    public static final String SETTINGS = "Settings";
    public static final String FRAGMENT_SETTINGS = "fragment_settings";

    public static final String SELECT_FILTER_LOWER = "select filter...";

    public static final String NEWS_DESK = "news_desk:(";
    public static final String ART = "\"Art\"";
    public static final String DINING = "\"Dining\"";
    public static final String FASHION = "\"Fashion & Style\"";
    public static final String HOME = "\"Home\"";
    public static final String MOVIES = "\"Movies\"";
    public static final String SPORTS = "\"Sports\"";
    public static final String CLOSE = ")";

    public static final String NETWORK_EXEC = "/system/bin/ping -c 1 8.8.8.8";

    public static final String NO_INTERNET_TITLE = "No Internet";
    public static final String NO_INTERNET_TEXT = "It seems that you are not connected to the internet. Make sure that you are connected before trying again";

    public static final String NO_ARTICLES_TITLE = "No results";
    public static final String NO_ARTICLES_TEXT = "No articles returned from your search. Please check your input and try again.";

}
