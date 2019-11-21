package com.example.paradiso.views;

import com.example.paradiso.pojo.detailedMovie.DetailedMovie;

import java.util.ArrayList;
import java.util.List;

public interface Favorite {
    void updateRecyclerView(List<DetailedMovie> movies);
    void displayProgressBar(boolean progressBarFlag);
    void showDetailedMovie(DetailedMovie detailedMovie);
    void getMovieDetail(int position);
    void makeToast(String message);
}
