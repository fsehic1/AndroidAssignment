package com.example.xxxx.whattowatchapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.xxxx.whattowatchapp.adapter.MyArrayAdapterMovie;
import com.example.xxxx.whattowatchapp.adapter.MyArrayAdapterTVShow;
import com.example.xxxx.whattowatchapp.fragment.MoviesFragment;
import com.example.xxxx.whattowatchapp.R;
import com.example.xxxx.whattowatchapp.helper.SectionsPageAdapter;
import com.example.xxxx.whattowatchapp.fragment.TVShowsFragment;
import com.example.xxxx.whattowatchapp.model.Movie;
import com.example.xxxx.whattowatchapp.model.TVShow;
import com.example.xxxx.whattowatchapp.service.GetMoviesApi;
import com.example.xxxx.whattowatchapp.service.GetTVShowsApi;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetMoviesApi.IMoviesQuery, GetTVShowsApi.ITVShowsQuery {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    MaterialSearchView searchView;

    ArrayList<Movie> moviesQuery;
    MyArrayAdapterMovie myArrayAdapterMovie;

    ArrayList<TVShow> tvShowsQuery;
    MyArrayAdapterTVShow myArrayAdapterTVShow;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("What to watch");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        final FrameLayout frame1 = (FrameLayout)findViewById(R.id.frame1);
        final FrameLayout frame2 = (FrameLayout)findViewById(R.id.frame2);
        listView = (ListView)findViewById(R.id.listView);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //setupViewPager(mViewPager);
        mSectionsPageAdapter.addFragment(new MoviesFragment(), "MOVIES");
        mSectionsPageAdapter.addFragment(new TVShowsFragment(), "TV SHOWS");
        mViewPager.setAdapter(mSectionsPageAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        searchView = (MaterialSearchView)findViewById(R.id.search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                frame1.setVisibility(View.GONE);
                frame2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                //If closed Search View , lstView will return default
                frame1.setVisibility(View.VISIBLE);
                frame2.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //frame1.setVisibility(View.VISIBLE);
                //frame2.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int tab_position=tabLayout.getSelectedTabPosition();
                if(newText.length() >= 3) {
                    if(tab_position == 0) {
                        //call api for movies
                        GetMoviesApi getMoviesApi = new GetMoviesApi(getBaseContext(), MainActivity.this);
                        getMoviesApi.getMoviesBySearch(newText);
                    } else {
                        //call api for TV shows
                        GetTVShowsApi getTVShowsApi = new GetTVShowsApi(getBaseContext(), MainActivity.this);
                        getTVShowsApi.getTVShowsBySearch(newText);
                    }
                } else {
                    if(tab_position == 0) {
                        if(moviesQuery != null) {
                            moviesQuery.clear();
                            myArrayAdapterMovie.notifyDataSetChanged();
                            moviesQuery = null;
                        }
                    } else {
                        if(tvShowsQuery != null) {
                            tvShowsQuery.clear();
                            myArrayAdapterTVShow.notifyDataSetChanged();
                            tvShowsQuery = null;
                        }
                    }
                }
                return true;
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DisplayMoreActivity.class);
                Movie movie;
                TVShow tvShow;
                if(moviesQuery != null) {
                    movie = moviesQuery.get(position);
                    intent.putExtra("movie",movie);
                } else if(tvShowsQuery != null) {
                    tvShow = tvShowsQuery.get(position);
                    intent.putExtra("tvShow",tvShow);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                //MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    public void getQueryMovies(ArrayList<Movie> result) {
        moviesQuery = result;
        myArrayAdapterMovie = new MyArrayAdapterMovie(this, R.layout.item_list, moviesQuery);
        listView.setAdapter(myArrayAdapterMovie);
        myArrayAdapterMovie.notifyDataSetChanged();
    }

    public void getQueryTVShows(ArrayList<TVShow> result) {
        tvShowsQuery = result;
        myArrayAdapterTVShow = new MyArrayAdapterTVShow(this, R.layout.item_list, tvShowsQuery);
        listView.setAdapter(myArrayAdapterTVShow);
        myArrayAdapterTVShow.notifyDataSetChanged();
    }

}