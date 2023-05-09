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
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.databinding.InfoFragmentBinding;
import com.example.moviemate.databinding.MapsFragmentBinding;
import com.example.moviemate.model.Location;
import com.example.moviemate.service.RetrofitClientMaps;
import com.example.moviemate.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends Fragment {

    Context context;

    double lat = 0.00;
    double longitutde = 0.00;
    private DatabaseReference mDatabaseRef;

    private MapsFragmentBinding binding;

     String address;
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

    private void readDataFromFirebase(View view, String email)
    {
        System.out.println("User login email id is : " + email);
        if(email == null)
            return;

        //Get the data before @ in the email address and consider it as the username of the user
        String username = email.split("@")[0];

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        Toast.makeText(getContext(), "Successfully Read", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String add = String.valueOf(dataSnapshot.child("address").getValue());
                        Log.d("Tag", add);
                        String addressToGeocode = "951-955,Dandenong road,Malvern East,Vic-3145";

                        String encodedAddress = null;
                        try {
                            encodedAddress = URLEncoder.encode(add, "UTF-8").replace("+", "%20");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        Log.d("list", String.valueOf(encodedAddress));

                        Call<Location> call = RetrofitClientMaps.getInstance().getMyApi().getCoordinates(encodedAddress,
                                "pk.eyJ1Ijoic2h3ZXR6aW5nIiwiYSI6ImNsaDZieThwZjA0aWozcXFtYWdncDBrOGEifQ.lg0qOE0d64Rfeh305RM2eQ");

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
                                                        new LatLng(lat, longitutde), 15));

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
                else {
                    //error occurs while fetching this data
                    Toast.makeText(getContext(), "Failed to read the data", Toast.LENGTH_SHORT).show();
                }
            }
        });



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
        UserViewModel model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        model.getLoginEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                System.out.println("email" + s);
                readDataFromFirebase(view, s);
            }
        });



    }
}

