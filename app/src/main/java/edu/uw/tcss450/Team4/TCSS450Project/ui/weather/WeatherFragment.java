package edu.uw.tcss450.Team4.TCSS450Project.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private FragmentWeatherBinding binding;
    static WeatherFragment instance;

    //city name and user's response

    //current today weather
    ImageView icon_weather;
    TextView text_weather_description, text_temperature, text_city_name;

    //hourly weather
    ImageView image_hour1,image_hour2,image_hour3,image_hour4,image_hour5,image_hour6,image_hour7,image_hour8,image_hour9,image_hour10,image_hour11,image_hour12;
    ImageView image_hour13,image_hour14,image_hour15,image_hour16,image_hour17,image_hour18,image_hour19,image_hour20,image_hour21,image_hour22,
            image_hour23,image_hour24;
    TextView text_hour1, text_hour2, text_hour3,text_hour4,text_hour5,text_hour6,text_hour7,text_hour8,text_hour9,text_hour10,text_hour11,text_hour12;
    TextView text_hour13, text_hour14, text_hour15,text_hour16,text_hour17,text_hour18,text_hour19,text_hour20,text_hour21,text_hour22,text_hour23,text_hour24;
    TextView text_temp1,text_temp2, text_temp3, text_temp4,text_temp5,text_temp6,text_temp7,text_temp8,text_temp9,text_temp10,text_temp11,text_temp12,
            text_temp13, text_temp14,text_temp15,text_temp16,text_temp17,text_temp18,text_temp19,text_temp20,text_temp21,text_temp22,text_temp23,text_temp24;

    //7-day forecast
    TextView text_day1,text_day2,text_day3,text_day4,text_day5,text_day6,text_day7;
    ImageView image_day1,image_day2,image_day3,image_day4,image_day5,image_day6,image_day7;
    TextView text_day1_temp,text_day2_temp,text_day3_temp,text_day4_temp,text_day5_temp,text_day6_temp,text_day7_temp;

    //weather conditions
    TextView text_humidity,text_wind_speed,text_sunrise,text_sunset;




    public static WeatherFragment getInstance(){
        if (instance==null){
            instance = new WeatherFragment();}
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }




}
