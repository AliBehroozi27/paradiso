package com.example.paradiso.roomDatabase;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Dao;


import com.example.paradiso.pojo.detailedMovie.DetailedMovie;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM f_movies")
    Maybe<List<DetailedMovie>> getAllFavoriteMovies();

    @Query("SELECT * FROM f_movies")
    List<DetailedMovie> getAll();

    @Query("SELECT * FROM f_movies WHERE imdbId =:imdbId")
    Maybe<DetailedMovie> getMovie(String imdbId);

    @Query("SELECT COUNT(*) FROM f_movies")
    int getSize();

    @Insert
    void insertMovie(DetailedMovie favoriteMovie);

    @Delete
    void deleteMovie(DetailedMovie detailedMovie);
}
