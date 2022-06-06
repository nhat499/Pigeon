package edu.uw.tcss450.Team4.TCSS450Project.model;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<Location> mLocation;
    private String latitude;
    private String longitude;
    private String cityName;



/** Getters and Setters  for longitude, latitude and city name */
    public String getLat (){
        return this.latitude;
    }
    public String getLon() {
        return this.longitude;
    }
    public void setLat(String Latitude){
        this.latitude = Latitude;
    }
    public void setLon(String Longitude){
        this.longitude = Longitude;
    }
    public String getCityName(){return this.cityName.toUpperCase();}
    public void setCityName(String name){ this.cityName = name; }

    public LocationViewModel() {

        mLocation = new MediatorLiveData<>();
    }


    public void addLocationObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

    public void setLocation(final Location location) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }

    public Location getCurrentLocation() {
        return new Location(mLocation.getValue());
    }

}
