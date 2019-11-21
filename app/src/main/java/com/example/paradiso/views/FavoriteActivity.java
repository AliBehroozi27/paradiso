package com.example.paradiso.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.paradiso.R;
import com.example.paradiso.adapters.FavoriteRecyclerViewAdapter;
import com.example.paradiso.adapters.MainRecyclerViewAdapter;
import com.example.paradiso.pojo.detailedMovie.DetailedMovie;
import com.example.paradiso.presenters.FavoritePresenter;
import com.example.paradiso.presenters.FavoritePresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity implements Favorite{
    @BindString(R.string.error_opening)
    String errorOpeningMessage;
    @BindString(R.string.favorite_activity_title)
    String favoriteActivityTitle;
    @BindView(R.id.recyclerView)
    RecyclerView rvFavorite;
    @BindView(R.id.progressBar)
    ProgressBar loadingProgressBar;

    private FavoriteRecyclerViewAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private FavoritePresenterImpl presenter;

    //constants
    private static final String MOVIE = "movie";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(favoriteActivityTitle);
        initialViews();
        presenter = new FavoritePresenterImpl(this ,this);

    }

    private void initialViews() {
        mAdapter = new FavoriteRecyclerViewAdapter(this, new ArrayList<DetailedMovie>() {}, this);
        layoutManager = new LinearLayoutManager(this);
        rvFavorite.setLayoutManager(layoutManager);
        rvFavorite.setAdapter(mAdapter);
        rvFavorite.setItemAnimator(new DefaultItemAnimator());
        rvFavorite.setHasFixedSize(true);
    }

    @Override
    public void updateRecyclerView(List<DetailedMovie> movies) {
        mAdapter.setMovies((ArrayList<DetailedMovie>)movies);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayProgressBar(boolean progressBarFlag) {
        if (progressBarFlag) {
            loadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            loadingProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showDetailedMovie(DetailedMovie movie) {
        Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
        intent.putExtra(MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(this , errorOpeningMessage , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getMovieDetail(int position) {
        presenter.showMovieProfile(position);
    }

    @Override
    protected void onDestroy() {
        presenter.dispose();
        super.onDestroy();
    }
    

    @Override
    protected void onStart() {
        Log.e("AAA" , "start");
        presenter.getFavoriteMoviesFromDB();
        super.onStart();
    }
}
