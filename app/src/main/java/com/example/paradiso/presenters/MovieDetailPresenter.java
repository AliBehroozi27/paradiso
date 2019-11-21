package com.example.paradiso.presenters;

import com.example.paradiso.pojo.detailedMovie.DetailedMovie;

public interface MovieDetailPresenter {
    void setViewsContent();
    void setMovie(DetailedMovie movie);
    void onFavoriteClick();
}
