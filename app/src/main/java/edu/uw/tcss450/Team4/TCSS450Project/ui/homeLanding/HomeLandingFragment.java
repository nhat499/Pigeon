package edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentHomeLandingBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.LocationViewModel;

/**
 * Class to define the fragment lifecycle for the HomeLanding Fragment
 *
 * @author team4
 * @version May 2022
 */
public class HomeLandingFragment extends Fragment {
    private FragmentHomeLandingBinding mBinding;
    private HomeLandingViewModel mHomeLandModel;
    private RecentMessageListViewModel mMessages;
    private HomeLandingViewModel weatherRespond;
    //The ViewModel that will store the current location
    private LocationViewModel mLocationModel;
    // map info
    private GoogleMap mMap;

    Geocoder geocoder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeLandingFragmentArgs  args = HomeLandingFragmentArgs
                .fromBundle(getActivity().getIntent().getExtras());

        mHomeLandModel = new ViewModelProvider(getActivity())
                .get(HomeLandingViewModel.class);
        mHomeLandModel.connect(args.getJwt(), args.getEmail()); // should be swap once fix

        weatherRespond = new ViewModelProvider(getActivity())
                .get(HomeLandingViewModel.class);

        mMessages = new ViewModelProvider(getActivity())
                .get(RecentMessageListViewModel.class);
        mMessages.connectGet(args.getEmail());

        List<Address> list;
        //mMap = GoogleMap
//        mLocationModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
//        mLocationModel.a
        // hard coded number that doesnt matter right now
        //weatherRespond.connectGetWeather(47.2529, 47.2529);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        mBinding = FragmentHomeLandingBinding.inflate(inflater);

        // Inflate the layout for this fragment
        return mBinding.getRoot();
//        return inflater.inflate(R.layout.fragment_home_landing, container, false);
    }

    private Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {

        HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        return BitmapFactory.decodeStream(input);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Observer<HashMap<String,String>> userInfoObserver = map -> {
//            mBinding.numOfContact.setText(map.get("numOfContact"));
//            mBinding.numOfMessage.setText(map.get("numOfMessages"));
            mBinding.profileName.setText(map.get("name"));
            mBinding.profileEmail.setText(map.get("email"));
        };
        mHomeLandModel.HomeLandingObserver(getViewLifecycleOwner(), userInfoObserver);

        mLocationModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);

        mLocationModel.addLocationObserver(getViewLifecycleOwner(), location -> {
            Log.d("location", "onViewCreated: " + location.toString());

            Log.d("LOCATION", "onViewCreated: " + " AAAAAAAA"  );

            geocoder = new Geocoder(getContext());
            try {
                List<Address> l =  geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                //Log.d("LOCATION", "onViewCreated: " + " AAAAAAAA"  + l.get(0).getLocality());
                mBinding.cityName.setText(l.get(0).getLocality());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //mBinding.cityName.setText(location.getLatitude() + " " + location.getLongitude());

            weatherRespond.connectGetWeather(location.getLatitude(), location.getLongitude());
        });

        weatherRespond.HomeLandingWeatherObserver(getViewLifecycleOwner(), data -> {
            Log.d("testtest", "onViewCreated: " + data.toString() + "   " + "temp" + "\u00B0");
            mBinding.textTemperature.setText(data.get("temp") + "\u00B0");
            mBinding.textWeatherDescription.setText(data.get("description"));

//            Thread newThread = new Thread(() -> {
//                try {
//                    //InputStream is = (InputStream) new URL("https://openweathermap.org/img/wn/" + data.get("icon")+ "@2x.png").getContent();
//                    InputStream is = (InputStream) new URL("https://openweathermap.org/img/wn/04n@2x.png").getContent();
//                    Drawable d = Drawable.createFromStream(is, "OpenWeather");
//                    mBinding.iconWeather.setImageDrawable(d);
//                } catch (Exception e) {
//                    Log.d("TESTTEST", "onViewCreated: EEEEEEEEEEEEERRRRRRRRRRRRORRRRRR" + e);
//
//
//                }
//            });



        });


        mMessages.addRecentMessageObserver(getViewLifecycleOwner(), m -> {
            mBinding.listRoot.setAdapter(
//                        new MessageRecylerViewAdapter(messageList)
                    new MessageRecylerViewAdapter(mMessages.getmRecentMessageList())
            );
        });


    }
}