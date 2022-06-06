package edu.uw.tcss450.Team4.TCSS450Project.ui.weather;

import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.location.Address;
import android.location.Geocoder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.LocationViewModel;

/**
 * Class to define the fragment lifecycle for the Weather Fragment
 *
 * @author team4
 * @version May 2022
 */
public class WeatherFragment extends Fragment {

    private WeatherViewModel mSendWeatherModel;
    private WeatherViewModel mSendWeatherModelHD;
    private WeatherViewModel mSendWeatherModelCity;
    private WeatherViewModel mSendWeatherModelZip;

    private FragmentWeatherBinding binding;
    //The ViewModel that will store the current location
    private LocationViewModel mLocationModel;


    // map info
    private GoogleMap mMap;
    Geocoder geocoder;

    static WeatherFragment instance;
    //hard coded longitude and latitude values
    //private String lat ="47.2529";
   // private String lon ="-122.4443";

    //empty constructor
    public WeatherFragment() {

    }

     public static WeatherFragment getInstance(){
       if (instance==null){
           instance = new WeatherFragment();}
        return instance;
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_weather, container, false);
        binding = FragmentWeatherBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSendWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mSendWeatherModelHD = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mSendWeatherModelCity = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mSendWeatherModelZip = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //hardCodedCity(); //sets to hard coded city : Tacoma


        binding.buttonSearchZip.setOnClickListener(button -> {
            viewZip();
        });
        binding.buttonSearchCity.setOnClickListener(button -> {
            viewCity();
        });
        binding.buttonSearchMap.setOnClickListener(button -> {
            viewMap();
        });
        binding.buttonSearchCurrent.setOnClickListener(button -> {
            viewCurrentLocation();
        });
                //viewZip(); //gets weather data for the zip code
                //viewCity(); //gets weather data for the city name
                //viewMap();  // gets weather data from the clicked map location
                //viewCurrentLocation(); // gets the weather data for the device location

    }//end of method


/** VIEW METHODS FOR: current location, zip location, city location, map location*/

    /**
     * This is a helper method for onViewCreated() that calls on things necessary to view current location.
     */
    private void viewCurrentLocation(){
        //For Current Location
        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);


        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if(location != null) {

                //Setting Latitude and Longitude value to current location
                mSendWeatherModel.setLat(String.valueOf(location.getLatitude()));
                mSendWeatherModel.setLon(String.valueOf(location.getLongitude()));
                mSendWeatherModelHD.setLat(String.valueOf(location.getLatitude()));
                mSendWeatherModelHD.setLon(String.valueOf(location.getLongitude()));

                // Calling the connect weather methods
                mSendWeatherModel.getConnectWeather();
                mSendWeatherModelHD.getConnectWeatherHD();

                //Calling observers methods
                mSendWeatherModel.addResponseObserver(getViewLifecycleOwner(), this::observeWeather);
                mSendWeatherModelHD.addResponseObserver(getViewLifecycleOwner(),this::observeWeatherHD);

                geocoder = new Geocoder(getContext());
                try {
                    List<Address> l =  geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                    //Log.d("LOCATION", "onViewCreated: " + l.get(0).getLocality());
                    binding.textCityName.setText(l.get(0).getLocality().toUpperCase());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sets the weather data to hard coded location: Tacoma
     */
    private void hardCodedCity(){
         String lat ="47.2529";
         String lon ="-122.4443";
         String city= "Tacoma";
        mSendWeatherModel.setLon(lon);
        mSendWeatherModelHD.setLon(lon);
        mSendWeatherModel.setLat(lat);
        mSendWeatherModelHD.setLat(lat);
        mSendWeatherModel.setCityName(city);
        mSendWeatherModelHD.setCityName(city);
        // Calling the connect weather methods
          mSendWeatherModel.getConnectWeather();
          mSendWeatherModelHD.getConnectWeatherHD();

        //Calling observers methods
        mSendWeatherModel.addResponseObserver(getViewLifecycleOwner(), this::observeWeather);
        mSendWeatherModelHD.addResponseObserver(getViewLifecycleOwner(),this::observeWeatherHD);
    }

    private void viewCity(){
        //button click listener for searching the city name
        Log.i("view city name", mSendWeatherModelCity.getCityName());
        binding.buttonSearchCity.setOnClickListener(button -> {
            if(binding.textEnterCityName.getText().toString().equals("") ||binding.textEnterCityName.getText().toString() == null
            || isInteger(binding.textEnterCityName.getText().toString())== true){

                binding.textEnterCityName.setText(null);
                binding.textEnterCityName.setHintTextColor(Color.BLUE);
                binding.textEnterCityName.setHint("Please Enter Valid City Name");

            } else {

                mSendWeatherModelCity.getConnectWeatherCity(binding.textEnterCityName.getText().toString());
                mSendWeatherModelHD.getConnectWeatherHD();
                mSendWeatherModel.getConnectWeather();
                mSendWeatherModel.setCityName(mSendWeatherModelCity.getCityName());

                mSendWeatherModelCity.addResponseObserver(getViewLifecycleOwner(),this::observeCityCoords);
                mSendWeatherModel.addResponseObserver(getViewLifecycleOwner(),this::observeWeather);
                mSendWeatherModelHD.addResponseObserver(getViewLifecycleOwner(),this::observeWeatherHD);

                //TESTING
            Log.i("Model Lon and lat values:cityview",mSendWeatherModel.getLat()+","+ mSendWeatherModel.getLon());
            Log.i("ModelHD Lon and lat values:cityview",mSendWeatherModelHD.getLat()+","+ mSendWeatherModelHD.getLon());
            Log.i("ModelCity Lon and lat values:cityview",mSendWeatherModelCity.getLat()+","+ mSendWeatherModelCity.getLon());
                Log.i("Model name :cityview",mSendWeatherModel.getCityName());
                Log.i("ModelHD name:cityview",mSendWeatherModelHD.getCityName());
                Log.i("ModelCity name:cityview",mSendWeatherModelCity.getCityName());

            }
        });
    }

    /**
     * This method is for the onViewCreated it listens for the search zip button to be click and
     * set the longitude and latitude to zip code location.
     */
    private void viewZip(){

        //button click listener for searching the zip code
        Log.i("View zip","ENTER!");
        binding.buttonSearchZip.setOnClickListener(button -> {
            Log.i("View zip","MADE IT!");
            if(binding.textEnterCityName.getText().toString().equals("")
                    ||binding.textEnterCityName.getText().toString() == null
                    ||isInteger(binding.textEnterCityName.getText().toString())== false){

                binding.textEnterCityName.setText(null);
                binding.textEnterCityName.setHintTextColor(Color.BLUE);
                binding.textEnterCityName.setHint("Please Enter Valid Zip Code");

            } else {
                //connect method
                mSendWeatherModelZip.getConnectWeatherZip(binding.textEnterCityName.getText().toString());
                mSendWeatherModelHD.getConnectWeatherHD();
                mSendWeatherModel.getConnectWeather();

                //observers
                mSendWeatherModelZip.addResponseObserver(getViewLifecycleOwner(),this::observeZipCoords);
                mSendWeatherModel.addResponseObserver(getViewLifecycleOwner(),this::observeWeather);
                mSendWeatherModelHD.addResponseObserver(getViewLifecycleOwner(),this::observeWeatherHD);


                //TESTING
                Log.i("Model Lon and lat values:zipview",mSendWeatherModel.getLat()+","+ mSendWeatherModel.getLon());
                Log.i("ModelHD Lon and lat values:zipview",mSendWeatherModelHD.getLat()+","+ mSendWeatherModelHD.getLon());
                Log.i("ModelCity Lon and lat values:zipview",mSendWeatherModelCity.getLat()+","+ mSendWeatherModelCity.getLon());
                Log.i("ModelZip Lon and lat values:zipview",mSendWeatherModelZip.getLat()+","+ mSendWeatherModelZip.getLon());

            }
        });

    } //end of method

    /**
     * onViewCreated() helper method to get the location from the map
     */
    private void viewMap(){
        Log.i("viewmap", "Enter");
        binding.buttonSearchMap.setOnClickListener(button -> {
                Log.i("viewmap onclick", "MADE IT");
                Navigation.findNavController(getView()).navigate(WeatherFragmentDirections.
                        actionNavigationWeatherToNavigationLocation());
                Log.i("viewmap onclick", "MADE IT AFTER NAV");

        });
        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if(location != null) {

                //Setting Latitude and Longitude value to current location
                mSendWeatherModel.setLat(String.valueOf(model.getLat()));
                mSendWeatherModel.setLon(String.valueOf(model.getLon()));
                mSendWeatherModelHD.setLat(String.valueOf(model.getLat()));
                mSendWeatherModelHD.setLon(String.valueOf(model.getLon()));

                // Calling the connect weather methods
                mSendWeatherModel.getConnectWeather();
                mSendWeatherModelHD.getConnectWeatherHD();

                //Calling observers methods
                mSendWeatherModel.addResponseObserver(getViewLifecycleOwner(), this::observeWeather);
                mSendWeatherModelHD.addResponseObserver(getViewLifecycleOwner(),this::observeWeatherHD);

                    Log.i("viewmap lat", String.valueOf(model.getLat()));
                    Log.i("viewmap lon", String.valueOf(model.getLon()));
                geocoder = new Geocoder(getContext());

                if(model.getLat()!=null && model.getLon()!=null ) {
                    Log.i("viewmap geocoder IF STATEMENT", "made it ");

                    try {
                        Log.i("viewmap geocoder try", "made it ");

                        List<Address> l = geocoder.getFromLocation(Double.valueOf(model.getLat()), Double.valueOf(model.getLon()), 1);
                        Log.i(" viewmap LOCATION", "onViewCreated: " + String.valueOf(l.get(0).getLocality()));
                        model.setCityName(String.valueOf(l.get(0).getLocality()));
                        binding.textCityName.setText(model.getCityName());

                        //Setting City Names of Longitude and Latitude values
                          mSendWeatherModel.setCityName(model.getCityName());
                          mSendWeatherModelHD.setCityName(model.getCityName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }});
    }


/** SET LAT, LON, & CITY NAME METHODS FOR: city name and zip code */

    /**
     * Gets the latitude and longitude of the city name
     * @param response
     * @throws JSONException
     */
    public void setCityLatLon(JSONObject response) throws JSONException{
        //To get the coordinates
        JSONObject jsonCoord = new JSONObject(response.getString("coord"));
        mSendWeatherModelCity.setLat(jsonCoord.getString("lat"));
        mSendWeatherModelCity.setLon(jsonCoord.getString("lon"));
        Log.i("setCITY COORDINATES", jsonCoord.toString());
        Log.i("ModelCity Lon and lat values:city",mSendWeatherModelCity.getLat()+","+ mSendWeatherModelCity.getLon());

        JSONObject root = response;
        mSendWeatherModelCity.setCityName(root.getString("name").toUpperCase());
        Log.i("setCity",mSendWeatherModelCity.getCityName());
        binding.textCityName.setText(mSendWeatherModelCity.getCityName());

    }

    /**
     * Getsthe latitude, longitude and the city name of the zip code
     * @param response
     * @throws JSONException
     */
    public void setZipLatLon(JSONObject response) throws JSONException{

        //To get the coordinates
        JSONObject jsonCoord = new JSONObject(response.getString("coord"));
        mSendWeatherModelZip.setLat(jsonCoord.getString("lat"));
        mSendWeatherModelZip.setLon(jsonCoord.getString("lon"));
        Log.i("setZip COORDINATES", jsonCoord.toString());
        Log.i("ModelCity Lon and lat values:zip",mSendWeatherModelZip.getLat()+","+ mSendWeatherModelZip.getLon());

        JSONObject root = response;
        mSendWeatherModelZip.setCityName(root.getString("name").toUpperCase());
        binding.textCityName.setText(mSendWeatherModelZip.getCityName());
    }


/** GET WEATHER METHODS FOR: current, HD */
    /**
     * This method gets the current weather and formats it.
     * @param response the json
     * @throws JSONException json
     */
    public void getCurrentWeather(JSONObject response) throws JSONException{

        //ADD CONDITIONS and ICON
        JSONObject jsonCurrent = new JSONObject(response.getString("current")); //gets the json message
        Log.i("TESTING", jsonCurrent.toString());
        binding.textTemperature.setText(String.valueOf(jsonCurrent.getString("temp") + "°F"));

        //gets the data array held in the 'weather' section
        JSONArray jsonWeatherArray = new JSONArray(jsonCurrent
                .getString("weather"));

        //converts the above array into a string to be cast into JSONObject
        String str = jsonWeatherArray.getString(0);

        //Casts into JSONObject and extract the description for the fragment
        String jsonWeatherDescription = new JSONObject(str).getString("description");
        binding.textWeatherDescription.setText(jsonWeatherDescription);

        // FOR ICON
        String jsonWeatherIcon = new JSONObject(str).getString("icon");
        String iconUrl = "http://openweathermap.org/img/w/" + jsonWeatherIcon + ".png";
        //ImageView image = (ImageView) getView().findViewById(R.id.icon_weather);
        //Picasso.get().load(iconUrl).into(image);
        Log.i("ICON STRING", iconUrl.toString());
        //binding.iconWeather.setImageIcon(iconUrl);
        //binding.iconWeather.setImageIcon(Picasso.get().load(iconUrl));
        // binding.iconWeather.setImageIcon((ImageView)Picasso.get().load(iconUrl));

        //CURRENT CONDITIONS
        DateFormat timeFormat = new SimpleDateFormat("h:mm a");

        binding.textHumidity.setText(jsonCurrent.getString("humidity")+" %\nHumidity");
        binding.textWindSpeed.setText(jsonCurrent.getString("wind_speed")+" mph\nWind Speed");
        binding.textSunrise.setText(timeFormat.format(new Date(Long.parseLong(jsonCurrent.getString("sunrise")) * 1000))+"\nSunrise");
        binding.textSunset.setText(timeFormat.format(new Date(Long.parseLong(jsonCurrent.getString("sunset")) * 1000))+"\nSunset");
        binding.textUvi.setText(jsonCurrent.getString("uvi")+"\n"+getUVI(jsonCurrent.getString("uvi"))+"\nUVI");
        binding.textFeelsLike.setText(jsonCurrent.getString("feels_like")+"°F\nFeels like");

    }

    /**
     * This method gets the 7 day and hourly forecasts and formats it.
     * @param response the json
     * @throws JSONException json
     */
    public void getWeatherHD(JSONObject response) throws JSONException{

        // ADDING THE TEXT VIEWS FOR HOURLY TEMPERATURE (24HRS)
        JSONArray jsonMessageHourly = response.getJSONArray("hourly");
        ArrayList<TextView> tempText = new ArrayList<TextView>() {
            {
                add(binding.textTemp1);
                add(binding.textTemp2);
                add(binding.textTemp3);
                add(binding.textTemp4);
                add(binding.textTemp5);
                add(binding.textTemp6);
                add(binding.textTemp7);
                add(binding.textTemp8);
                add(binding.textTemp9);
                add(binding.textTemp10);
                add(binding.textTemp11);
                add(binding.textTemp12);
                add(binding.textTemp13);
                add(binding.textTemp14);
                add(binding.textTemp15);
                add(binding.textTemp16);
                add(binding.textTemp17);
                add(binding.textTemp18);
                add(binding.textTemp19);
                add(binding.textTemp20);
                add(binding.textTemp21);
                add(binding.textTemp22);
                add(binding.textTemp23);
                add(binding.textTemp24);
            }
        };

        // ADDING TEXT VIEWS FOR THE (UNIX TIME) (24 HOURS)
        ArrayList<TextView> hoursText = new ArrayList<TextView>() {
            {
                add(binding.textHour1);
                add(binding.textHour2);
                add(binding.textHour3);
                add(binding.textHour4);
                add(binding.textHour5);
                add(binding.textHour6);
                add(binding.textHour7);
                add(binding.textHour8);
                add(binding.textHour9);
                add(binding.textHour10);
                add(binding.textHour11);
                add(binding.textHour12);
                add(binding.textHour13);
                add(binding.textHour14);
                add(binding.textHour15);
                add(binding.textHour16);
                add(binding.textHour17);
                add(binding.textHour18);
                add(binding.textHour19);
                add(binding.textHour20);
                add(binding.textHour21);
                add(binding.textHour22);
                add(binding.textHour23);
                add(binding.textHour24);
            }
        };

        //ADD ICON DATA LATER
        ArrayList<ImageView> hourIcons = new ArrayList<ImageView>() {
            {
                add(binding.imageHour1);
                add(binding.imageHour2);
                add(binding.imageHour3);
                add(binding.imageHour4);
                add(binding.imageHour5);
                add(binding.imageHour6);
                add(binding.imageHour7);
                add(binding.imageHour8);
                add(binding.imageHour9);
                add(binding.imageHour10);
                add(binding.imageHour11);
                add(binding.imageHour12);
                add(binding.imageHour13);
                add(binding.imageHour14);
                add(binding.imageHour15);
                add(binding.imageHour16);
                add(binding.imageHour17);
                add(binding.imageHour18);
                add(binding.imageHour19);
                add(binding.imageHour20);
                add(binding.imageHour21);
                add(binding.imageHour22);
                add(binding.imageHour23);
                add(binding.imageHour24);
            }
        };


        // ADDING MANUALLY THE HOURLY DATA

        DateFormat timeFormat = new SimpleDateFormat("h a");
        ArrayList<String> unixTime = new ArrayList<String>();

        for (int i = 0; i < 24; i++) {
            JSONObject hour = jsonMessageHourly.getJSONObject(i);
            tempText.get(i).setText(hour.getString("temp") + "\n°F"); // Adding hourly temps
            unixTime.add(hour.getString("dt")); //getting unix time
            hoursText.get(i).setText(timeFormat.format(new Date(Long.parseLong(unixTime.get(i)) * 1000))); //adding the unix time w/format
        }
        hoursText.get(0).setText("Now");

        Log.i("HOURLY TEMPS", tempText.toString());

        //ADDING THE TEXT VIEW FOR TEMPERATURES OF THE WEEK
        JSONArray jsonMessageDaily = response.getJSONArray("daily");
        ArrayList<TextView> dailyTempText = new ArrayList<TextView>() {
            {
                add(binding.textDay1Temp);
                add(binding.textDay2Temp);
                add(binding.textDay3Temp);
                add(binding.textDay4Temp);
                add(binding.textDay5Temp);
                add(binding.textDay6Temp);
                add(binding.textDay7Temp);
            }
        };
        //ADDING THE TEXT VIEW FOR THE DAYS OF THE WEEK

        ArrayList<TextView> nameOfDays = new ArrayList<TextView>() {
            {
                add(binding.textDay1);
                add(binding.textDay2);
                add(binding.textDay3);
                add(binding.textDay4);
                add(binding.textDay5);
                add(binding.textDay6);
                add(binding.textDay7);
            }
        };

        ArrayList<TextView> dailyTempTextHL = new ArrayList<TextView>() {
            {
                add(binding.textDay1Hilo);
                add(binding.textDay2Hilo);
                add(binding.textDay3Hilo);
                add(binding.textDay4Hilo);
                add(binding.textDay5Hilo);
                add(binding.textDay6Hilo);
                add(binding.textDay7Hilo);
            }
        };

        //MANUALLY ADDING THE WEEKLY FORECAST DATA
        DateFormat df = new SimpleDateFormat("EE M/d");
        Calendar cal = Calendar.getInstance();
        DateFormat timeFormat2 = new SimpleDateFormat("EEE, MMM d, yyyy");

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.textDate.setText(timeFormat2.format(cal.getTime()));
        // FOR THE CALANDER DAYS
        for (int i = 1; i < 7; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            nameOfDays.get(i).setText(df.format(cal.getTime()));
        }
        // FOR THE WEEKLY TEMPERATURES
         for (int i = 0; i < 7; i++) {
         JSONObject day = jsonMessageDaily.getJSONObject(i);
         JSONObject temp = day.getJSONObject("temp");

        dailyTempText.get(i).setText(temp.getString("min") + " °"); // gets the min (low) temps
        dailyTempTextHL.get(i).setText(temp.getString("max") + " °"); // gets the max (high) temps
         }

    }//end of method

/** OBSERVER METHODS FOR: weather, weatherHD, city, zip */
    /**
     * Gets the weather information for the current conditions.
     * @param response the json
     */
    private void observeWeather(JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textTemperature.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    getCurrentWeather(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * This method gets the weather conditions for both the 7 day and hourly temperatures.
     * @param response the json
     */
    private void observeWeatherHD(JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textTemperature.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    getWeatherHD(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * This method gets the coordinates for the zip codes
     * @param response the json
     */
    private void observeZipCoords(JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textTemperature.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error obs", e.getMessage());
                }
            } else {
                try {
                    Log.i("obsZip","MADE IT");
                    setZipLatLon(response);
                    Log.i("obsZip","MADE IT OUT");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
    /**
     * This method is an observer for the city model
     * @param response the json
     */
    private void observeCityCoords(JSONObject response) {
        Log.i("Observe city coords", "MADE IT!");
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textTemperature.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    setCityLatLon(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

/** EXTRA HELPER METHODS */

    /**
     * This is a helper method that gives the label and the value of the uvi ranges.
     * This method is used in getWeather()
     * @param uvi
     * @return
     */
    public String getUVI(String uvi){
        double d = Double.parseDouble(uvi);
        if(d<3)return"Low";
        if(3<=d && d<=5)return "Moderate";
        if (5<d && d<=7) return "High";
        if(7<d && d<=10) return "Very High";
        if(d>10) return "Extreme";
        return uvi;
    }

    /**
     * This is a helper method that checks if a string is an integer value.
     * This is for the checking if zip code input is numeric(an integer)
     * @param s : String
     * @return
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

} //end of class