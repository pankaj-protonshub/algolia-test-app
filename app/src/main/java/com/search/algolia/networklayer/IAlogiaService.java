package com.search.algolia.networklayer;

import com.search.algolia.models.Hits;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IAlogiaService {


        @GET("search_by_date")
        Call<Hits> getStories(
                @Query("tags") String tags,
                @Query("page") int page
        );

}
