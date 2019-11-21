package com.example.paradiso.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.paradiso.R;
import com.example.paradiso.pojo.detailedMovie.DetailedMovie;
import com.example.paradiso.presenters.MovieDetailPresenterImpl;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetail {
    @BindString(R.string.add_to_favorite)
    String addMessage;
    @BindString(R.string.remove_from_favorite)
    String removeMessage;
    @BindString(R.string.movie)
    String MOVIE;
    @BindString(R.string.base_url)
    String BASE_URL;
    @BindString(R.string.add_to_favorite)
    String add_to_favorite_message;
    @BindView(R.id.backDrop)
    ImageView backDropPoster;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.overViewContent)
    TextView overView;
    @BindView(R.id.rating)
    RatingBar ratingBar;
    @BindView(R.id.numericalRating)
    TextView numericalRaring;
    @BindView(R.id.genresContent)
    TextView genres;
    @BindView(R.id.releasedDateContent)
    TextView releasedDate;
    @BindView(R.id.countryContent)
    TextView country;

    private DetailedMovie movie;
    private MovieDetailPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        presenter = new MovieDetailPresenterImpl(this, this);

        initialToolbar();
        movie = getMovie();
        presenter.setMovie(movie);
        presenter.setViewsContent();

    }

    @OnClick(R.id.fab)
    void onFabClick(View view) {
        presenter.onFavoriteClick();
    }


    private DetailedMovie getMovie() {
        Bundle bundle = getIntent().getExtras();
        return (DetailedMovie) bundle.getSerializable(MOVIE);

    }

    private void initialToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void setMovieContents(DetailedMovie movie) {
        getSupportActionBar().setTitle(movie.getTitle());
        Picasso.get().load(BASE_URL + movie.getBackdropPath()).into(backDropPoster);
        overView.setText(movie.getOverview());
        ratingBar.setRating(movie.getVoteAverage() / 2);
        numericalRaring.setText(movie.getVoteAverage() + "/10");
        country.setText(getCountries());
        genres.setText(getGenres());
        releasedDate.setText(movie.getReleaseDate());

    }

    @Override
    public void showSnackBar(String title, boolean isAdding) {
        String message = title;
        if (isAdding) {
            message += addMessage;
        } else {
            message += removeMessage;
        }
        Snackbar.make(fab, message, Snackbar.LENGTH_SHORT).show();
    }

    private String getGenres() {
        String gnrs = "";
        for (int i = 0; i < movie.getGenres().size(); i++) {
            if (i != movie.getGenres().size() - 1)
                gnrs += movie.getGenres().get(i).getName() + " / ";
            else
                gnrs += movie.getGenres().get(i).getName();
        }
        return gnrs;
    }

    private String getCountries() {
        String counteries = "";
        for (int i = 0; i < movie.getProductionCountries().size(); i++) {
            if (i != movie.getProductionCountries().size() - 1)
                counteries += movie.getProductionCountries().get(i).getName() + " / ";
            else
                counteries += movie.getProductionCountries().get(i).getName();
        }
        return counteries;
    }

    @Override
    protected void onDestroy() {
        presenter.dispose();
        super.onDestroy();
    }
}
