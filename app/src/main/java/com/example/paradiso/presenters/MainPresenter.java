package com.example.paradiso.presenters;

import com.example.paradiso.pojo.Movie;
import com.example.paradiso.pojo.detailedMovie.DetailedMovie;

import java.util.ArrayList;

public interface MainPresenter {
    void getQuery(String query);

    void updateViews(Integer state, ArrayList<Movie> movies);

    void getTrendingMovies();

    void dispose();

    void getMovieDetail(Integer id);

    void showMovieProfile(DetailedMovie detailedMovie);

    void loadMoreMovie(int pageNumber);

    void setIsTrending(Boolean isTrending);

    void makeToast(String message);

}
