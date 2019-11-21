package com.example.paradiso.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.paradiso.ApiUtils;
import com.example.paradiso.R;
import com.example.paradiso.pojo.detailedMovie.DetailedMovie;
import com.example.paradiso.pojo.Movie;
import com.example.paradiso.pojo.Result;
import com.example.paradiso.remote.MovieService;
import com.example.paradiso.views.FavoriteActivity;
import com.example.paradiso.views.Main;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.ButterKnife;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainPresenterImpl implements MainPresenter {
    MovieService movieSrvice;
    Context context;
    Main mainView;
    CompositeDisposable compositeDisposable;

    //constants
    private static final String API_KEY = "643fd3ee9abf73bbc9af06ce9d12a607";
    private static final String TIME = "week";
    private static final String TREND_TYPE = "movie";
    private static final String SEARCH_TYPE = "movie";

    //states
    private static final int FETCH_DATA = 1;
    private static final int SHOW_DATA = 2;
    private static final int SHOW_DETAILS = 3;
    private static final int ERROR_OPENING = 4;


    private PublishSubject<String> trendingSubject;
    private ArrayList<Movie> trendingMovies;
    private ArrayList<Movie> queriedMovies;
    private PublishSubject<String[]> querySubject;
    private Boolean isTrending;
    private String query;

    public MainPresenterImpl(Main mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
        initialVariables();
        movieSrvice = ApiUtils.getMovieService();
        getTrendingMovies();
    }

    private void initialVariables() {
        trendingMovies = new ArrayList<Movie>() {
        };
        queriedMovies = new ArrayList<Movie>() {
        };
        compositeDisposable = new CompositeDisposable();
        trendingSubject = getTrendingSubject();
        querySubject = getQuerySubject();
        isTrending = true;

    }

    @Override
    public void getQuery(String query) {
        queriedMovies.clear();
        this.query = query;
        String[] queryContent = new String[]{query, "1"};
        querySubject.onNext(queryContent);
    }


    @Override
    public void updateViews(Integer state, ArrayList<Movie> movies) {
        switch (state) {
            case FETCH_DATA:
                mainView.setLoading(true);
                mainView.displayProgressBar(true);
                break;
            case SHOW_DETAILS:
                mainView.setLoading(false);
                mainView.displayProgressBar(false);
                break;
            case SHOW_DATA:
                mainView.updateRecyclerView(movies);
                mainView.displayProgressBar(false);
                mainView.setLoading(false);
                break;
            case ERROR_OPENING:
                mainView.makeToast("");
                mainView.displayProgressBar(false);
                break;

        }
    }

    @Override
    public void getTrendingMovies() {
        updateViews(FETCH_DATA, null);
        if (trendingMovies.size() == 0) {
            String queryPage = "1";
            trendingSubject.onNext(queryPage);
        } else {
            updateViews(SHOW_DATA, trendingMovies);
        }
    }

    @Override
    public void dispose() {
        compositeDisposable.dispose();
    }

    @Override
    public void getMovieDetail(Integer id) {
        updateViews(FETCH_DATA, null);
        movieSrvice.getMovieDetail(id, API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMovieDetailObserver());
    }

    private Observer<DetailedMovie> getMovieDetailObserver() {
        return new Observer<DetailedMovie>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(DetailedMovie detailedMovie) {
                updateViews(SHOW_DETAILS, null);
                showMovieProfile(detailedMovie);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("bbbb" , e + "");
                updateViews(ERROR_OPENING , null);
            }

            @Override
            public void onComplete() {
            }
        };
    }

    @Override
    public void showMovieProfile(DetailedMovie detailedMovie) {
        mainView.showMovieDetail(detailedMovie);
    }

    @Override
    public void loadMoreMovie(int pageNumber) {
        updateViews(FETCH_DATA, null);
        if (isTrending) {
            trendingSubject.onNext(pageNumber + "");
        } else {
            String[] queryContent = new String[]{query, pageNumber + ""};
            querySubject.onNext(queryContent);
        }
    }

    @Override
    public void setIsTrending(Boolean isTrending) {
        this.isTrending = isTrending;
    }

    @Override
    public void makeToast(String message) {
        mainView.makeToast(message);
    }

    public PublishSubject<String> getTrendingSubject() {
        if (this.trendingSubject == null) {
            this.trendingSubject = PublishSubject.create();
            this.trendingSubject
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .switchMap(new Function<String, ObservableSource<Result>>() {
                        @Override
                        public ObservableSource<Result> apply(String page) throws Exception {
                            return movieSrvice.getTrendingMovies(TREND_TYPE, TIME, API_KEY, page).subscribeOn(Schedulers.io());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Result>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Result result) {
                            trendingMovies.addAll(result.getMovies());
                            updateViews(SHOW_DATA, trendingMovies);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("bbbb", e + " trend");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
        return this.trendingSubject;
    }

    public PublishSubject<String[]> getQuerySubject() {
        if (this.querySubject == null) {
            this.querySubject = PublishSubject.create();
            this.querySubject
                    .switchMap(new Function<String[], ObservableSource<Result>>() {
                        @Override
                        public ObservableSource<Result> apply(String[] values) throws Exception {
                            return movieSrvice.queryMovies(SEARCH_TYPE, values[0], API_KEY, values[1]).subscribeOn(Schedulers.io());
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Result>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Result result) {
                            queriedMovies.addAll(result.getMovies());
                            updateViews(SHOW_DATA, queriedMovies);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("bbbb", e + "");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
        return this.querySubject;
    }

    public void showFavorites() {
        mainView.showFavoriteActivity();
    }
}

