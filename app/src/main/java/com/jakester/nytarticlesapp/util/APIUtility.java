package com.jakester.nytarticlesapp.util;

import com.jakester.nytarticlesapp.interfaces.NYTArticlesService;
import com.jakester.nytarticlesapp.network.RestClient;

/**
 * Created by Jake on 6/22/2017.
 */

public class APIUtility {

    public static NYTArticlesService getArticleService() {
        return RestClient.getClient().create(NYTArticlesService.class);
    }
}
