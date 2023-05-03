package com.example.moviemate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import com.mapbox.geojson.Point;

import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.Plugin;


public class MapActivity extends Fragment {
    private MapView mapView;
    private MapboxMap mapboxMap;

    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       /* this.context = container.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.maps_fragment, container, false);*/

        View view = inflater.inflate(R.layout.maps_fragment,container,false);

        final Point point = Point.fromLngLat(145.045837, -37.876823 );
        mapView = view.findViewById(R.id.mapView);

        CameraOptions cameraPosition = new CameraOptions.Builder()
                .zoom(13.0)
                .center(point)
                .build();
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
        mapView.getMapboxMap().setCamera(cameraPosition);

        return view;
    }


    public static MapActivity newInstance()
    {
        MapActivity fragment = new MapActivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}

