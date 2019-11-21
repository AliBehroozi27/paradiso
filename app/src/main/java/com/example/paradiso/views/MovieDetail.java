package com.example.paradiso.views;

import com.example.paradiso.pojo.detailedMovie.DetailedMovie;

public interface MovieDetail {
    void setMovieContents(DetailedMovie movie);
    void showSnackBar(String message, boolean isAdding);
}
