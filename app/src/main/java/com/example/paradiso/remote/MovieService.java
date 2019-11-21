package com.example.paradiso.remote;

import com.example.paradiso.pojo.detailedMovie.DetailedMovie;
import com.example.paradiso.pojo.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("trending/{trend_type}/{time}")
    Observable<Result> getTrendingMovies(
            @Path("trend_type") String trendType,
            @Path("time") String time,
            @Query("api_key") String apiKey,
            @Query("page") String page
            );

    @GET("search/{search_type}")
    Observable<Result> queryMovies(
            @Path("search_type") String searchType,
            @Query("query") String query,
            @Query("api_key") String apiKey,
            @Query("page") String page
    );

    @GET("movie/{id}")
    Observable<DetailedMovie> getMovieDetail(
            @Path("id") int id,
            @Query("api_key") String apiKey
    );


}
