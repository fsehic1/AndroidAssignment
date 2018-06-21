package com.example.xxxx.whattowatchapp.service;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

public class GetTVShowsApi {

    public interface ITVShows {
        public void getTopTVShows(ArrayList<TVShow> result);
    }

    public interface ITVShowsQuery {
        public void getQueryTVShows(ArrayList<TVShow> result);
    }

    private final static String APIKey = "2900f516e3474a372a73dedbd3752bd5";
    private final Context context;
    private final ITVShows itvShows;
    private final ITVShowsQuery itvShowsQuery;

    public GetTVShowsApi(Context context, ITVShows itvShows) {
        this.context = context;
        this.itvShows = itvShows;
        this.itvShowsQuery = null;
    }

    public GetTVShowsApi(Context context, ITVShowsQuery itvShowsQuery) {
        this.context = context;
        this.itvShows = null;
        this.itvShowsQuery = itvShowsQuery;
    }

    public void getTopTVShows() {
        if(itvShows == null)
            return;

        String URL = "https://api.themoviedb.org/3/tv/top_rated?api_key=" + APIKey + "&language=en-US";

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray items = jo.getJSONArray("results");
                    String itemsResponse = items.toString();
                    TVShow[] tvShowsArray = gson.fromJson(itemsResponse, TVShow[].class);
                    ArrayList<TVShow> tvShows = new ArrayList<TVShow>();
                    for(int i = 0; i < 10; i++) {
                        tvShows.add(tvShowsArray[i]);
                    }
                    itvShows.getTopTVShows(tvShows);
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

    public void getTVShowsBySearch(String query) {
        if(itvShowsQuery == null)
            return;

        String URL = "https://api.themoviedb.org/3/search/tv?api_key=" + APIKey + "&language=en-US&query=" + query;

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray items = jo.getJSONArray("results");
                    String itemsResponse = items.toString();
                    TVShow[] tvShowsArray = gson.fromJson(itemsResponse, TVShow[].class);
                    ArrayList<TVShow> movies = new ArrayList<TVShow>(Arrays.asList(tvShowsArray));
                    itvShowsQuery.getQueryTVShows(movies);
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
