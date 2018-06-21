package com.example.xxxx.whattowatchapp.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.xxxx.whattowatchapp.R;
import com.example.xxxx.whattowatchapp.activity.DisplayMoreActivity;
import com.example.xxxx.whattowatchapp.adapter.MyArrayAdapterMovie;
import com.example.xxxx.whattowatchapp.model.Movie;
import com.example.xxxx.whattowatchapp.model.TVShow;
import com.example.xxxx.whattowatchapp.service.GetMoviesApi;

import java.util.ArrayList;

public class MoviesFragment extends Fragment implements GetMoviesApi.IMovies {

    private static final String TAG = "MoviesFragment";

    private ArrayList<Movie> movies;
    private MyArrayAdapterMovie myArrayAdapterMovie;
    ListView listView;
    GetMoviesApi getMoviesApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment,container,false);

        movies = new ArrayList<Movie>();
        getMoviesApi = new GetMoviesApi(getContext(), MoviesFragment.this);
        listView = (ListView)view.findViewById(R.id.listView);

        getMoviesApi.getTopMovies();
        myArrayAdapterMovie = new MyArrayAdapterMovie(view.getContext(),R.layout.item_list, movies);
        listView.setAdapter(myArrayAdapterMovie);
        myArrayAdapterMovie.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DisplayMoreActivity.class);
                Movie movie;
                movie = movies.get(position);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        return view;
    }

    public void getTopMovies(ArrayList<Movie> result) {
        movies = result;
        myArrayAdapterMovie = new MyArrayAdapterMovie(getContext(),R.layout.item_list, movies);
        listView.setAdapter(myArrayAdapterMovie);
    }

}
