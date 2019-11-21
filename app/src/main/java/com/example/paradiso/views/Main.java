package com.example.paradiso.views;


import com.example.paradiso.pojo.detailedMovie.DetailedMovie;
import com.example.paradiso.pojo.Movie;

import java.util.ArrayList;

public interface Main {
    void updateRecyclerView(ArrayList<Movie> movies);
    void displayProgressBar(Boolean showFlag);
    void showMovieDetail(DetailedMovie movie);
    void getMovieDetail(Integer id);
    void setLoading(Boolean isLoading);
    void showFavoriteActivity();
    void makeToast(String message);
}
