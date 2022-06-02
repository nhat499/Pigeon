package edu.uw.tcss450.Team4.TCSS450Project.ui.weather;

import android.media.Image;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentWeatherBinding;

/**
 * Class to define the fragment lifecycle for the Weather Fragment
 *
 * @author team4
 * @version May 2022
 */
public class WeatherFragment extends Fragment {

    private WeatherViewModel mSendWeatherModel;
    private WeatherViewModel mSendWeatherModelHD;
    private FragmentWeatherBinding binding;
    static WeatherFragment instance;
    private String lat ="47.2529";
    private String lon ="-122.4443";

    //empty constructor
    public WeatherFragment() {

    }

    // public static WeatherFragment getInstance(){
    //   if (instance==null){
    //       instance = new WeatherFragment();}
    //    return instance;
    // }

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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSendWeatherModel.getConnectWeather();
        mSendWeatherModelHD.getConnectWeatherHD();
        mSendWeatherModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeWeather);
        mSendWeatherModelHD.addResponseObserver(getViewLifecycleOwner(),this::observeWeatherHD);

        // button click listener
      binding.buttonSearchCity.setOnClickListener(button -> Navigation.findNavController(getView()).
                navigate(WeatherFragmentDirections.actionNavigationWeatherToNavigationLocation()));

    }

    /**
     * This method gets the current weather and formats it.
     * @param response the json
     * @throws JSONException json
     */
    public void getCurrentWeather(JSONObject response) throws JSONException{
        //To get the message json
        //ADD CONDITIONS and ICON
        JSONObject jsonCurrent = new JSONObject(response.getString("current"));
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

        String jsonWeatherIcon = new JSONObject(str).getString("icon");
       // binding.iconWeather.setImageIcon();

        //CONDITIONS
        DateFormat timeFormat = new SimpleDateFormat("h:mm a");

        binding.textHumidity.setText(jsonCurrent.getString("humidity")+" %\nHumidity");
        binding.textWindSpeed.setText(jsonCurrent.getString("wind_speed")+" mph\nWind Speed");
        binding.textSunrise.setText(timeFormat.format(new Date(Long.parseLong(jsonCurrent.getString("sunrise")) * 1000))+"\nSunrise");
        binding.textSunset.setText(timeFormat.format(new Date(Long.parseLong(jsonCurrent.getString("sunset")) * 1000))+"\nSunset");




    }

    /**
     * This method gets the 7 day and hourly forecasts and formats it.
     * @param response the json
     * @throws JSONException json
     */
    public void getWeatherHD(JSONObject response) throws JSONException{

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



        for (int i = 0; i < 24; i++) {
            JSONObject hour = jsonMessageHourly.getJSONObject(i);
            tempText.get(i).setText(hour.getString("temp") + "\n°F");
        }
        Log.i("HOURLY TEMPS", tempText.toString());

        //Temperatures for the days of the week
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

        for (int i = 0; i < 7; i++) {
            JSONObject day = jsonMessageDaily.getJSONObject(i);
            JSONObject temp = day.getJSONObject("temp");
            dailyTempText.get(i).setText(temp.getString("day") + " °F");
        }
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

        //Manually adding the next 7 days date
        DateFormat df = new SimpleDateFormat("EE M/d");
        Calendar cal = Calendar.getInstance();
        DateFormat timeFormat2 = new SimpleDateFormat("EEE, MMM d, yyyy");

        //String tod = df.format(today)
        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.textDate.setText(timeFormat2.format(cal.getTime()));
        for (int i = 1; i < 7; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            nameOfDays.get(i).setText(df.format(cal.getTime()));
        }


        // Adding manually the hours data

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


        DateFormat timeFormat = new SimpleDateFormat("h a");
        ArrayList<String> unixTime = new ArrayList<String>();

        for (int i = 0; i < 24; i++) {
            JSONObject hour = jsonMessageHourly.getJSONObject(i);
            unixTime.add(hour.getString("dt")); //getting unix time
            hoursText.get(i).setText(timeFormat.format(new Date(Long.parseLong(unixTime.get(i)) * 1000)));
        }
            hoursText.get(0).setText("Now");



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
        for (int i = 0; i < 24; i++) {
           // JSONObject hour = jsonMessageHourly.getJSONObject(i);
           // unixTime.add(hour.getString("dt")); //getting unix time
           // hoursText.get(i).setText(timeFormat.format(new Date(Long.parseLong(unixTime.get(i)) * 1000)));
        }



    }//end of method


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
}