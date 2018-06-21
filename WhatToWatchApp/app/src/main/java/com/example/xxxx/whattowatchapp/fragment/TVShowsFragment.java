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
import com.example.xxxx.whattowatchapp.adapter.MyArrayAdapterTVShow;
import com.example.xxxx.whattowatchapp.model.TVShow;
import com.example.xxxx.whattowatchapp.service.GetTVShowsApi;

import java.util.ArrayList;

public class TVShowsFragment extends Fragment implements GetTVShowsApi.ITVShows {

    private static final String TAG = "TVShowsFragment";

    private ArrayList<TVShow> tvShows;
    private MyArrayAdapterTVShow myArrayAdapterTVShow;

    ListView listView;
    GetTVShowsApi getTVShowsApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment,container,false);

        tvShows = new ArrayList<TVShow>();
        getTVShowsApi = new GetTVShowsApi(getContext(), TVShowsFragment.this);
        listView = (ListView)view.findViewById(R.id.listView);

        getTVShowsApi.getTopTVShows();
        myArrayAdapterTVShow = new MyArrayAdapterTVShow(view.getContext(), R.layout.item_list, tvShows);
        listView.setAdapter(myArrayAdapterTVShow);
        myArrayAdapterTVShow.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DisplayMoreActivity.class);
                TVShow tvShow;
                tvShow = tvShows.get(position);
                intent.putExtra("tvShow", tvShow);
                startActivity(intent);
            }
        });

        return view;
    }

    public void getTopTVShows(ArrayList<TVShow> result) {
        tvShows = result;
        myArrayAdapterTVShow = new MyArrayAdapterTVShow(getContext(),R.layout.item_list, tvShows);
        listView.setAdapter(myArrayAdapterTVShow);
    }

}
