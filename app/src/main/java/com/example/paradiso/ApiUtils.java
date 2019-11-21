package com.example.paradiso;

import com.example.paradiso.remote.MovieService;
import com.example.paradiso.remote.RetrofitClient;

public class ApiUtils {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static MovieService getMovieService() {
        return RetrofitClient.getClient(BASE_URL).create(MovieService.class);
    }
}
