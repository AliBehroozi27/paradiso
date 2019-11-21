package com.example.paradiso.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paradiso.R;
import com.example.paradiso.pojo.Movie;
import com.example.paradiso.pojo.detailedMovie.DetailedMovie;
import com.example.paradiso.views.Favorite;
import com.example.paradiso.views.Main;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder> {

    private Favorite mView;
    public List<DetailedMovie> movies;
    private final Context context;
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w500";
    private Integer chosenMovieId;
    private int checkedPosition;


    public FavoriteRecyclerViewAdapter(Context context, List<DetailedMovie> movies, Favorite favorite) {
        this.movies = movies;
        this.context = context;
        mView = favorite;
    }

    public void setMovies(ArrayList<DetailedMovie> movies) {
        this.movies = movies;
    }

    public void addMovies(ArrayList<DetailedMovie> movies) {
        this.movies.addAll(movies);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailedMovie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        Picasso.get().load(BASE_URL + movie.getPosterPath()).into(holder.poster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView.getMovieDetail(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public int getChosenItem() {
        Log.e("BBBB", "" + chosenMovieId);

        return chosenMovieId;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.poster)
        ImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

