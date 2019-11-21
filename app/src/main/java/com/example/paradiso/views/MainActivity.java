package com.example.paradiso.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.paradiso.R;
import com.example.paradiso.adapters.MainRecyclerViewAdapter;
import com.example.paradiso.pojo.Movie;
import com.example.paradiso.pojo.detailedMovie.DetailedMovie;
import com.example.paradiso.presenters.MainPresenterImpl;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements Main, android.widget.SearchView.OnQueryTextListener, SearchView.OnQueryTextListener, RecyclerView.OnScrollChangeListener {
    @BindString(R.string.error_opening)
    String errorOpeningMessage;
    @BindString(R.string.movie)
    String MOVIE;
    @BindView(R.id.recyclerView)
    RecyclerView trendsRv;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.progressBar)
    ProgressBar loadingProgressBar;

    private MainRecyclerViewAdapter mAdapter;
    private MainPresenterImpl presenter;
    private int totalItemCount;
    private LinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private boolean loading;
    private int pageNumber = 1;

    //constants
    private static final int VISIBLE_THRESHOLD = 1;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialRecyclerView();
        presenter = new MainPresenterImpl(this, this);
        searchView.setOnQueryTextListener(this);
    }


    private void initialRecyclerView() {
        mAdapter = new MainRecyclerViewAdapter(this, new ArrayList<Movie>() {
        }, this);
        layoutManager = new LinearLayoutManager(this);
        trendsRv.setLayoutManager(layoutManager);
        trendsRv.setAdapter(mAdapter);
        trendsRv.setItemAnimator(new DefaultItemAnimator());
        trendsRv.setOnScrollChangeListener(this);
        trendsRv.setHasFixedSize(true);
    }

    @OnClick(R.id.recyclerView)
    public void onRecyclerViewClick() {
        Integer chosenItemId = mAdapter.getChosenItem();
        Log.e("BBBB", "" + chosenItemId);
        presenter.getMovieDetail(chosenItemId);
    }

    @Override
    public void updateRecyclerView(ArrayList<Movie> movies) {
        mAdapter.setMovies(movies);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayProgressBar(Boolean showFlag) {
        if (showFlag) {
            loadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            loadingProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showMovieDetail(DetailedMovie movie) {
        Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
        intent.putExtra(MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void getMovieDetail(Integer id) {
        presenter.getMovieDetail(id);
    }


    @Override
    public void setLoading(Boolean isLoading) {
        this.loading = isLoading;
    }

    @Override
    public void showFavoriteActivity() {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(this, errorOpeningMessage, Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onQueryTextSubmit(String s) {
        if (!s.isEmpty()) {
            presenter.getQuery(s);
            presenter.setIsTrending(false);

        } else {
            presenter.getTrendingMovies();
            presenter.setIsTrending(true);
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onQueryTextChange(String s) {
        if (!s.isEmpty()) {
            presenter.getQuery(s);
            presenter.setIsTrending(false);
        } else {
            presenter.getTrendingMovies();
            presenter.setIsTrending(true);
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        presenter.dispose();
        super.onDestroy();
    }


    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
        totalItemCount = layoutManager.getItemCount();
        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        Log.e("aaa", loading + "  " + lastVisibleItem);

        if (!loading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
            pageNumber++;
            presenter.loadMoreMovie(pageNumber);
            loading = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                presenter.showFavorites();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }
}

