package com.jakester.nytarticlesapp.interfaces;

import com.jakester.nytarticlesapp.models.Response;
import com.jakester.nytarticlesapp.util.NYTConstants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Jake on 9/18/2017.
 */

public interface NYTArticlesService {
    @GET(NYTConstants.EXTENDED_URL)
    Observable<Response> getArticles(@Query("q") String query, @Query("page") int page,
                                     @Query("begin_date") String date, @Query("sort") String sortBy,
                                     @Query("fq") String newsDesk);
}
