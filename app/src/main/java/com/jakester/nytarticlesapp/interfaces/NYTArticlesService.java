package com.jakester.nytarticlesapp.interfaces;

import com.jakester.nytarticlesapp.models.Response;
import com.jakester.nytarticlesapp.util.NYTConstants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Jake on 9/18/2017.
 */

public interface NYTArticlesService {
    @GET(NYTConstants.EXTENDED_URL)
    Call<Response> getArticles(@Query("q") String query);
}
