package edu.uw.tcss450.Team4.TCSS450Project.ui.weather;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentLocationBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.LocationViewModel;

public class LocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private LocationViewModel mModel;
    private GoogleMap mMap;
    private FragmentLocationBinding binding;



    public LocationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textLatLong.setText("Click the map to get weather data of marker's location!");

        mModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        Log.i("OnviewCReated viewmap", "made it before the observer");

        //mModel.addLocationObserver(getViewLifecycleOwner(), location -> binding.textLatLong.setText(location.toString()));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //add this fragment as the OnMapReadyCallback -> See onMapReady()
        mapFragment.getMapAsync(this);
    }



   @SuppressLint("MissingPermission") // Don't know if this is supposed to be done
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        LocationViewModel model = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if(location != null) {
                Log.i("onMapReady","made it in oberver");
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);
                final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
                //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15.0f));
            }
        });
    }


    @Override
    public void onMapClick(LatLng latLng) {
        Log.i("LAT/LONG", latLng.toString());

        mMap.clear();

        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));
        Log.i("viewmap marker Latitude", String.valueOf(marker.getPosition().latitude));
        Log.i("viewmap marker Longitude", String.valueOf(marker.getPosition().longitude));
        mModel.setLat(String.valueOf(marker.getPosition().latitude));
        mModel.setLon(String.valueOf(marker.getPosition().longitude));


        Log.i("viewmap marker model lat", mModel.getLat());
        Log.i("viewmap marker model lon", mModel.getLat());

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        latLng, mMap.getCameraPosition().zoom));
        Log.i("viewmap Location Model lat and lon onMapCLick", mModel.getLat() +", "+ mModel.getLon());


    }

}