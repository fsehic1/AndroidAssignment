package com.example.xxxx.whattowatchapp.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xxxx.whattowatchapp.R;
import com.example.xxxx.whattowatchapp.model.Movie;
import com.example.xxxx.whattowatchapp.model.TVShow;
import com.squareup.picasso.Picasso;

import static java.security.AccessController.getContext;

public class DisplayMoreActivity extends AppCompatActivity {

    private static final String imageURLPath = "http://image.tmdb.org/t/p/w185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_more);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");
        TVShow tvShow = intent.getParcelableExtra("tvShow");

        TextView textViewTitle = (TextView) findViewById(R.id.title);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        TextView textViewOverview = (TextView) findViewById(R.id.overview);
        TextView textViewPopularity = (TextView) findViewById(R.id.popularity);
        TextView textViewReleaseDate = (TextView) findViewById(R.id.releaseDate);

        //in case of tvShow
        TextView textViewFirstAirDate = (TextView) findViewById(R.id.releaseDateText);

        if(movie != null) {
            textViewTitle.setText(movie.getTitle());
            Picasso.get().load(imageURLPath + movie.getPosterPath()).into(imageView);
            if(imageView.getDrawable() == null) {
                imageView.setImageBitmap(((BitmapDrawable) ContextCompat.getDrawable(getBaseContext(), R.drawable.movie_icon)).getBitmap());
            }
            textViewOverview.setText(movie.getOverview());
            textViewPopularity.setText(String.valueOf(movie.getPopularity()));
            textViewReleaseDate.setText(movie.getReleaseDate());
        } else if(tvShow != null) {
            textViewTitle.setText(tvShow.getName());
            Picasso.get().load(imageURLPath + tvShow.getPosterPath()).into(imageView);
            if(imageView.getDrawable() == null) {
                imageView.setImageBitmap(((BitmapDrawable) ContextCompat.getDrawable(getBaseContext(), R.drawable.tv_show_icon)).getBitmap());
            }
            textViewOverview.setText(tvShow.getOverview());
            textViewPopularity.setText(String.valueOf(tvShow.getPopularity()));
            textViewReleaseDate.setText(tvShow.getFirstAirDate());
            textViewFirstAirDate.setText(R.string.first_air_date);
        }

    }


}