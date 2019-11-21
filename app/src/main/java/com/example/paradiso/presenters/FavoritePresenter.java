package com.example.paradiso.presenters;

import com.example.paradiso.pojo.detailedMovie.DetailedMovie;

import java.util.List;

public interface FavoritePresenter {
    void getFavoriteMoviesFromDB();
    void updateView(int showData, List<DetailedMovie> detailedMovies);
    void showMovieProfile(int position);
    void dispose();
}
