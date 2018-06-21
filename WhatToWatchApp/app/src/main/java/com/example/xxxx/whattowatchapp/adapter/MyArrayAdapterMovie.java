package com.example.xxxx.whattowatchapp.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xxxx.whattowatchapp.R;
import com.example.xxxx.whattowatchapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyArrayAdapterMovie extends ArrayAdapter<Movie> {

    int resource;
    private static final String imageURLPath = "http://image.tmdb.org/t/p/w185";

    public MyArrayAdapterMovie(Context context, int _resource, List<Movie> items) {
        super(context, _resource, items);
        resource = _resource;
    }
    //resource is id of layout of itemList

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout newView;
        if (convertView == null) {
            newView = new RelativeLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (RelativeLayout)convertView;
        }

        Movie movie = getItem(position);

        CircleImageView image = (CircleImageView) newView.findViewById(R.id.image);
        TextView title = (TextView) newView.findViewById(R.id.title);
        URL url;
        try {
            url = new URL(imageURLPath + movie.getURLImage());
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return newView;
        }
        Picasso.get().load(url.toString()).into(image);

        if(image.getDrawable() == null) {
            image.setImageBitmap(((BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.movie_icon)).getBitmap());
        }

        title.setText(movie.getTitle());

        return newView;
    }

}
