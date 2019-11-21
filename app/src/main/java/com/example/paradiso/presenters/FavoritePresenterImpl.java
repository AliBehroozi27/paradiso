package com.example.paradiso.presenters;

import android.content.Context;
import android.util.Log;

import com.example.paradiso.R;
import com.example.paradiso.pojo.detailedMovie.DetailedMovie;
import com.example.paradiso.roomDatabase.FavoriteDao;
import com.example.paradiso.roomDatabase.FavoriteMoviesDatabase;
import com.example.paradiso.views.Favorite;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavoritePresenterImpl implements FavoritePresenter {
    private final Context context;
    private final Favorite favoriteView;
    private FavoriteDao roomDatabase;
    private CompositeDisposable compositeDisposable;
    Subscription subscription;
    private List<DetailedMovie> favoriteMovies;

    //states
    private static final int FETCH_DATA = 1;
    private static final int SHOW_DATA = 2;
    private static final int SHOW_DETAILS = 3;
    private static final int ERROR_OPENING = 4;


    public FavoritePresenterImpl(Favorite favoriteView, Context context) {
        this.context = context;
        this.favoriteView = favoriteView;
        initial();
    }

    private void initial() {
        roomDatabase = FavoriteMoviesDatabase.getInstance(context).favoriteDao();
        favoriteMovies = new ArrayList<DetailedMovie>() {
        };
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getFavoriteMoviesFromDB() {
        updateView(FETCH_DATA, null);
        roomDatabase.getAllFavoriteMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<DetailedMovie>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<DetailedMovie> favorites) {
                        Log.e("AAA", favorites.size() + "");
                        favoriteMovies = favorites;
                        updateView(SHOW_DATA, favorites);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("AAA", e + "");
                        updateView(ERROR_OPENING , null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void showMovieProfile(int position) {
        favoriteView.showDetailedMovie(favoriteMovies.get(position));
    }

    @Override
    public void dispose() {
        compositeDisposable.dispose();
    }

    @Override
    public void updateView(int state, List<DetailedMovie> favoriteMovies) {
        switch (state) {
            case FETCH_DATA:
                favoriteView.displayProgressBar(true);
                break;
            case SHOW_DETAILS:
                favoriteView.displayProgressBar(false);
                break;
            case SHOW_DATA:
                favoriteView.updateRecyclerView(favoriteMovies);
                favoriteView.displayProgressBar(false);
                break;
            case ERROR_OPENING:
                favoriteView.makeToast("");
                favoriteView.displayProgressBar(false);
                break;
        }
    }
}
