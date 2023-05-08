package com.example.moviemate;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moviemate.model.Location;
import com.example.moviemate.service.RetrofitClientMaps;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends Fragment {

    Context context;

    double lat = 0.00;
    double longitutde = 0.00;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        this.context = container.getContext();
        Mapbox.getInstance(this.context, getResources().getString(R.string.mapbox_access_token));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.maps_fragment, container, false);
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
        String addressToGeocode = "951-955, Dandenong road, Malvern East, Vic-3145";

        Call<Location> call = RetrofitClientMaps.getInstance().getMyApi().getLatLong();

        MapView mapView = view.findViewById(R.id.mapView);

        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                Location list = response.body();
                Log.d("list", String.valueOf(list.getLocationDetails().get(0).getCenter()));
                longitutde = (double) list.getLocationDetails().get(0).getCenter().get(0);
                lat = (double) list.getLocationDetails().get(0).getCenter().get(1);
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull MapboxMap mapboxMap) {
                        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                style.addImage("red-pin-marker", BitmapUtils.getBitmapFromDrawable(
                                        getResources().getDrawable(R.drawable.location)));
                                style.addLayer(new SymbolLayer("icon-layer-id", "icon-source-id").withProperties(
                                        iconImage("red-pin-marker"),
                                        iconIgnorePlacement(true),
                                        iconAllowOverlap(true),
                                        iconOffset(new Float[]{0f, -9f})
                                ));
                                GeoJsonSource iconGeoJsonSource = new GeoJsonSource("icon-source-id",
                                        Feature.fromGeometry(Point.fromLngLat(longitutde,lat)));
                                style.addSource(iconGeoJsonSource);
                                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lat, longitutde), 15.7));

                            }
                        });
                    }
                });

            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {

            }

        });

    }
}

