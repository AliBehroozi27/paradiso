package com.example.paradiso.presenters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.paradiso.R;
import com.example.paradiso.pojo.detailedMovie.DetailedMovie;
import com.example.paradiso.roomDatabase.FavoriteDao;
import com.example.paradiso.roomDatabase.FavoriteMoviesDatabase;
import com.example.paradiso.views.MovieDetail;
import com.example.paradiso.views.MovieDetailActivity;

import butterknife.BindString;
import butterknife.ButterKnife;
import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MovieDetailPresenterImpl implements MovieDetailPresenter {

    private final Context context;
    private final MovieDetail movieDetailView;
    private FavoriteDao roomDatabase;
    private DetailedMovie movie;
    private CompositeDisposable compositeDisposable;

    public MovieDetailPresenterImpl(MovieDetail movieDetailView, Context context) {
        this.context = context;
        this.movieDetailView = movieDetailView;
        initial();
    }

    private void initial() {
        roomDatabase = FavoriteMoviesDatabase.getInstance(context).favoriteDao();
        compositeDisposable = new CompositeDisposable();
    }


    @Override
    public void setViewsContent() {
        movieDetailView.setMovieContents(movie);
    }

    @Override
    public void setMovie(DetailedMovie movie) {
        this.movie = movie;
    }

    @Override
    public void onFavoriteClick() {
        roomDatabase.getMovie(movie.getImdbId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<DetailedMovie>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(DetailedMovie movie) {
                        Log.e("BBB", "found");
                        removeFromFavorites(movie);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("BBB", "riid");
                    }

                    @Override
                    public void onComplete() {
                        addToFavorite(movie);
                    }
                });
    }

    private void addToFavorite(DetailedMovie addingMovie) {
        Single<String> addObserver = Single.create(emitter -> {
            roomDatabase.insertMovie(addingMovie);
            emitter.onSuccess(addingMovie.getTitle());
        });
        addObserver
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(String s) {
                        movieDetailView.showSnackBar(s ,true);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    private void removeFromFavorites(DetailedMovie removingMovie) {
        Single<String> removeObserver = Single.create(emitter -> {
            roomDatabase.deleteMovie(removingMovie);
            emitter.onSuccess(removingMovie.getTitle());
        });
        removeObserver
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(String s) {
                        movieDetailView.showSnackBar(s,false);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    public void dispose() {
        compositeDisposable.dispose();
    }
}
