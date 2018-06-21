package com.example.xxxx.whattowatchapp.service;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.xxxx.whattowatchapp.model.Movie;
import com.example.xxxx.whattowatchapp.model.TVShow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class GetMoviesApi {

    public interface IMovies {
        public void getTopMovies(ArrayList<Movie> result);

    }

    public interface IMoviesQuery {
        public void getQueryMovies(ArrayList<Movie> result);
    }

    private final static String APIKey = "2900f516e3474a372a73dedbd3752bd5";
    private final Context context;
    private final IMovies iMovies;
    public final IMoviesQuery iMoviesQuery;

    public GetMoviesApi(Context context, IMovies iMovies) {
        this.context = context;
        this.iMovies = iMovies;
        this.iMoviesQuery = null;
    }

    public GetMoviesApi(Context context, IMoviesQuery iMoviesQuery) {
        this.context = context;
        this.iMovies = null;
        this.iMoviesQuery = iMoviesQuery;
    }

    public void getTopMovies() {
        if(iMovies == null)
            return;

        String URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + APIKey + "&language=en-US";

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray items = jo.getJSONArray("results");
                    String itemsResponse = items.toString();
                    Movie[] moviesArray = gson.fromJson(itemsResponse, Movie[].class);
                    ArrayList<Movie> movies = new ArrayList<Movie>();
                    for(int i = 0; i < 10; i++) {
                        movies.add(moviesArray[i]);
                    }
                    iMovies.getTopMovies(movies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }

    public void getMoviesBySearch(String query) {
        if(iMoviesQuery == null)
            return;

        String URL = "https://api.themoviedb.org/3/search/movie?api_key=" + APIKey + "&language=en-US&query=" + query;

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray items = jo.getJSONArray("results");
                    String itemsResponse = items.toString();
                    Movie[] moviesArray = gson.fromJson(itemsResponse, Movie[].class);
                    ArrayList<Movie> movies = new ArrayList<Movie>(Arrays.asList(moviesArray));
                    iMoviesQuery.getQueryMovies(movies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }

}
