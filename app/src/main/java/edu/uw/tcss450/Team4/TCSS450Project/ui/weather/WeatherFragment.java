package edu.uw.tcss450.Team4.TCSS450Project.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    //private String cityName;

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
        return inflater.inflate(R.layout.fragment_weather, container, false);

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

    }

    /**
     * This method gets the current weather and formats it.
     * @param response the json
     * @throws JSONException json
     */
    public void getCurrentWeather(JSONObject response) throws JSONException{
        //To get the message json
        JSONObject jsonMessage = new JSONObject(response.getString("WeatherInfo"));
        Log.i("TESTING", jsonMessage.toString());

        //binding.cityName.setText(response.getString("message"));
        //binding.weatherCityCountry.setText(jsonMessage.getString("name"));

        //to get the main json, then the temp
        JSONObject jsonMain = new JSONObject(jsonMessage.getString("main"));
        Float kelvin = Float.parseFloat(jsonMain.getString("temp"));
        Log.i("json", jsonMessage.toString());
        int temperature = (int)convertToFar(kelvin);
        binding.textTemperature.setText(String.valueOf(temperature + "°F"));

        //gets the data array held in the 'weather' section
        JSONArray jsonWeatherArray = new JSONArray(jsonMessage
                .getString("weather"));

        //converts the above array into a string to be cast into JSONObject
        String str = jsonWeatherArray.getString(0);

        //Casts into JSONObject and extract the description for the fragment
        String jsonWeather = new JSONObject(str).getString("description");
        binding.textWeatherDescription.setText(jsonWeather);
    }

    /**
     * This method gets the 7 day and hourly forecasts and formats it.
     * @param response the json
     * @throws JSONException json
     */
    public void getMultipleWeather(JSONObject response) throws JSONException{
        ArrayList<Integer> temps = new ArrayList<Integer>();
        ArrayList<String> descriptions = new ArrayList<String>();

        JSONObject jsonMessage = new JSONObject(response.getString("message"));
        Log.i("DAILY", jsonMessage.getString("daily"));
        JSONArray jsonDaily = jsonMessage.getJSONArray("daily");

        // loops for the weather for 7 days
        for(int i = 0; i < 7; i++) {
            JSONObject jsonDay = new JSONObject(jsonDaily.getString(i));
            JSONObject jsonTemp = new JSONObject(jsonDay.getString("temp"));
            Float kelvin = Float.parseFloat(jsonTemp.getString("day"));
            int temperature = (int)convertToFar(kelvin);
            temps.add(temperature);
        }



        Log.i("TEMPS", temps.toString());
        Log.i("DESCS", descriptions.toString());

        DateFormat df = new SimpleDateFormat("EE MMM dd:   ");
        Calendar cal = Calendar.getInstance();
        //String tod = df.format(today);

        //Manually add the 5 day forecast
        binding.textDay1.setText(df.format(cal.getTime()));
        binding.textDay1Temp.setText(temps.get(0) + "°F");

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.textDay2.setText(df.format(cal.getTime()));
        binding.textDay2Temp.setText(temps.get(1) + "°F");

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.textDay3.setText(df.format(cal.getTime()));
        binding.textDay3Temp.setText(temps.get(2) + "°F");

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.textDay4.setText(df.format(cal.getTime()));
        binding.textDay4Temp.setText(temps.get(3) + "°F");

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.textDay5.setText(df.format(cal.getTime()));
        binding.textDay5Temp.setText(temps.get(4) + "°F");

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.textDay6.setText(df.format(cal.getTime()));
        binding.textDay6Temp.setText(temps.get(5) + "°F");

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.textDay7.setText(df.format(cal.getTime()));
        binding.textDay7Temp.setText(temps.get(6) + "°F");

        //the hours (data)
        ArrayList<Integer> hourTemps = new ArrayList<Integer>();
        ArrayList<String> unixTime = new ArrayList<String>();
        ArrayList<String> hourDescriptions = new ArrayList<String>();
        JSONArray jsonHourly = jsonMessage.getJSONArray("hourly");

        //The temps per hour
        for(int i = 0; i < 24; i++) {
            JSONObject jsonInnerHourly = new JSONObject(jsonHourly.getString(i));
            //getting unix time
            unixTime.add(jsonInnerHourly.getString("dt"));
            //rest
            String thisTemp = jsonInnerHourly.getString("temp");
            Float kelvin = Float.parseFloat(thisTemp);
            int temperature = (int)convertToFar(kelvin);
            hourTemps.add(temperature);
        }

        //ADD ICON DATA LATER


        Log.i("HOURLY TEMPS", hourTemps.toString());

        Log.i("UNIX TIME", unixTime.toString());
        Date time = new Date(Long.parseLong(unixTime.get(0)) * 1000);
        String exp = time.toString();
        //Log.i("UNIX TIME!!!!!", exp);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss -    ");
        Log.i("TIME", timeFormat.format(time));

        //time of day in hour:min:sec
        Date hour1 = new Date(Long.parseLong(unixTime.get(0)) * 1000);
        Date hour2 = new Date(Long.parseLong(unixTime.get(1)) * 1000);
        Date hour3 = new Date(Long.parseLong(unixTime.get(2)) * 1000);
        Date hour4 = new Date(Long.parseLong(unixTime.get(3)) * 1000);
        Date hour5 = new Date(Long.parseLong(unixTime.get(4)) * 1000);
        Date hour6 = new Date(Long.parseLong(unixTime.get(5)) * 1000);
        Date hour7 = new Date(Long.parseLong(unixTime.get(6)) * 1000);
        Date hour8 = new Date(Long.parseLong(unixTime.get(7)) * 1000);
        Date hour9 = new Date(Long.parseLong(unixTime.get(8)) * 1000);
        Date hour10 = new Date(Long.parseLong(unixTime.get(9)) * 1000);
        Date hour11 = new Date(Long.parseLong(unixTime.get(10)) * 1000);
        Date hour12 = new Date(Long.parseLong(unixTime.get(11)) * 1000);
        Date hour13 = new Date(Long.parseLong(unixTime.get(12)) * 1000);
        Date hour14 = new Date(Long.parseLong(unixTime.get(13)) * 1000);
        Date hour15 = new Date(Long.parseLong(unixTime.get(14)) * 1000);
        Date hour16 = new Date(Long.parseLong(unixTime.get(15)) * 1000);
        Date hour17 = new Date(Long.parseLong(unixTime.get(16)) * 1000);
        Date hour18 = new Date(Long.parseLong(unixTime.get(17)) * 1000);
        Date hour19 = new Date(Long.parseLong(unixTime.get(18)) * 1000);
        Date hour20 = new Date(Long.parseLong(unixTime.get(19)) * 1000);
        Date hour21 = new Date(Long.parseLong(unixTime.get(20)) * 1000);
        Date hour22 = new Date(Long.parseLong(unixTime.get(21)) * 1000);
        Date hour23 = new Date(Long.parseLong(unixTime.get(22)) * 1000);
        Date hour24 = new Date(Long.parseLong(unixTime.get(23)) * 1000);

        //ADD ICONS LATER!!

        binding.textHour1.setText(timeFormat.format(hour1));
        binding.textTemp1.setText(hourTemps.get(0) + "°F");

        binding.textHour2.setText(timeFormat.format(hour2));
        binding.textTemp2.setText(hourTemps.get(1) + "°F");

        binding.textHour3.setText(timeFormat.format(hour3));
        binding.textTemp3.setText(hourTemps.get(2) + "°F");

        binding.textHour4.setText(timeFormat.format(hour4));
        binding.textTemp4.setText(hourTemps.get(3) + "°F");

        binding.textHour5.setText(timeFormat.format(hour5));
        binding.textTemp5.setText(hourTemps.get(4) + "°F");

        binding.textHour6.setText(timeFormat.format(hour6));
        binding.textTemp6.setText(hourTemps.get(5) + "°F");

        binding.textHour7.setText(timeFormat.format(hour7));
        binding.textTemp7.setText(hourTemps.get(6) + "°F");

        binding.textHour8.setText(timeFormat.format(hour8));
        binding.textTemp8.setText(hourTemps.get(7) + "°F");

        binding.textHour9.setText(timeFormat.format(hour9));
        binding.textTemp9.setText(hourTemps.get(8) + "°F");

        binding.textHour10.setText(timeFormat.format(hour10));
        binding.textTemp10.setText(hourTemps.get(9) + "°F");

        binding.textHour11.setText(timeFormat.format(hour11));
        binding.textTemp11.setText(hourTemps.get(10) + "°F");

        binding.textHour12.setText(timeFormat.format(hour12));
        binding.textTemp12.setText(hourTemps.get(11) + "°F");

        binding.textHour13.setText(timeFormat.format(hour13));
        binding.textTemp13.setText(hourTemps.get(12) + "°F");

        binding.textHour14.setText(timeFormat.format(hour14));
        binding.textTemp14.setText(hourTemps.get(13) + "°F");

        binding.textHour15.setText(timeFormat.format(hour15));
        binding.textTemp15.setText(hourTemps.get(14) + "°F");

        binding.textHour16.setText(timeFormat.format(hour16));
        binding.textTemp16.setText(hourTemps.get(15) + "°F");

        binding.textHour17.setText(timeFormat.format(hour2));
        binding.textTemp17.setText(hourTemps.get(16) + "°F");

        binding.textHour18.setText(timeFormat.format(hour2));
        binding.textTemp18.setText(hourTemps.get(17) + "°F");

        binding.textHour19.setText(timeFormat.format(hour19));
        binding.textTemp19.setText(hourTemps.get(18) + "°F");

        binding.textHour20.setText(timeFormat.format(hour20));
        binding.textTemp20.setText(hourTemps.get(19) + "°F");

        binding.textHour21.setText(timeFormat.format(hour21));
        binding.textTemp21.setText(hourTemps.get(20) + "°F");

        binding.textHour22.setText(timeFormat.format(hour22));
        binding.textTemp22.setText(hourTemps.get(21) + "°F");

        binding.textHour23.setText(timeFormat.format(hour23));
        binding.textTemp23.setText(hourTemps.get(22) + "°F");

        binding.textHour24.setText(timeFormat.format(hour2));
        binding.textTemp24.setText(hourTemps.get(23) + "°F");
    } //end of method


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
                    getMultipleWeather(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }


    /**
     * This method Converts Kelvin to Farenheit
     * @param theInt
     * @return long
     */
    public long convertToFar(Float theInt) {
        //If 9.0 and 5.0 not stated as doubles then they will use int division and result in 1
        double temp = (theInt * (9.0/5.0)) - 459.67;
        return Math.round(temp);
    }



}
