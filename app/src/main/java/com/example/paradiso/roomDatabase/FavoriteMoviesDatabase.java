package com.example.paradiso.roomDatabase;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.paradiso.pojo.Converters;
import com.example.paradiso.pojo.detailedMovie.DetailedMovie;

@Database(entities = DetailedMovie.class, version = 1)
@TypeConverters(Converters.class)
public abstract class FavoriteMoviesDatabase extends RoomDatabase {
    private static FavoriteMoviesDatabase instance;

    public abstract FavoriteDao favoriteDao();

    public static FavoriteMoviesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FavoriteMoviesDatabase.class,
                    "favorite_movies")
                    .build();
        }
        return instance;
    }

}
