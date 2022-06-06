package edu.uw.tcss450.Team4.TCSS450Project.ui.weather;

import android.app.Application;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.LocationViewModel;

/**
 * View model for the Weather Fragment
 * This class is going to hold the result of the weather data.
 * @author team4
 * @version May 2022
 */
public class WeatherViewModel extends AndroidViewModel {
    //This instance field is where we would get the response
    private MutableLiveData<JSONObject> mResponse;
    private MutableLiveData<JSONObject> mResponseHD; // data for hours and days
    private MutableLiveData<JSONObject> mResponseCity;
    private MutableLiveData<JSONObject> mResponseZip; // data for hours and days


    // Hard coded longitude and latitude values from:
    private String lat ="47.2529";
    private String lon ="-122.4443";
    private String cityName= "Tacoma";

    //private List<WeatherViewModel> weather;

    /**
     * Constructor: gives initial values to the instance fields.
     * @param application
     */
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mResponseHD = new MutableLiveData<>();
        mResponseHD.setValue(new JSONObject());
        mResponseCity = new MutableLiveData<>();
        mResponseCity.setValue(new JSONObject());
        mResponseZip = new MutableLiveData<>();
        mResponseCity.setValue(new JSONObject());
    }
/**
 * Getters and setters for latitude and longitude values and city name string value.
 */
    public String getLat (){
    return this.lat;
    }
    public String getLon() {
        return this.lon;
    }
    public void setLat(String Latitude){
        this.lat = Latitude;
    }
    public void setLon(String Longitude){
        this.lon = Longitude;
    }

    public String getCityName(){return this.cityName.toUpperCase();}
    public void setCityName(String name){ this.cityName = name; }

    /**
     * This method provides client code that adds observers to the LiveData
     *
     * @param owner
     * @param observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
        mResponseHD.observe(owner, observer);
        mResponseCity.observe(owner,observer);
        mResponseZip.observe(owner,observer);

    }

    /**
     * \
     * This method is the handler for error
     * @param result
     */
    private void handleResult(final JSONObject result) {

        mResponse.setValue(result);

    }

    private void handleResultHD(final JSONObject result) {
        mResponseHD.setValue(result);
    }

    /**
     * Gets and sets the Latitude and Longitude from city name and
     * @param result
     */
    private void handleResultCity(final JSONObject result) {

        Log.i("handle result city", "Entered");
        mResponseCity.setValue(result);
        /**
        try{
            JSONObject coord = result.getJSONObject("coord");
            String cityLat = coord.getString("lat");
            String cityLon = coord.getString("lon");
            setLat(cityLat);
            setLon(cityLon);
            Log.i("City Latitude handle result", cityLat.toString()+", "+cityLon.toString());
        }catch(JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle city weather");
        }
         **/


    }

    private void handleResultZip(final JSONObject result) {
        Log.i("zip handle result", "ENTERED");
        mResponseZip.setValue(result);
    }

    /**
     * These are the handle Error methods
     * @param error
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{" + "code:" + error.networkResponse.statusCode + ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    private void handleErrorHD(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponseHD.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponseHD.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
    private void handleErrorCity(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponseCity.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponseCity.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    private void handleErrorZip(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponseZip.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponseZip.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }


    /**
     * These are the getConnect methods
     */

    public void getConnectWeather() {
        try {
            Log.i("testing:", "Here");
            String url = getApplication().getResources().getString(R.string.base_url) + "weather/location/"+lat+"/"+lon;

            Request request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null, //no body for this get request
                    this::handleResult, this::handleError);
            request.setRetryPolicy(new DefaultRetryPolicy(10_000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Instantiate the RequestQueue and add the request to the queue
            Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getConnectWeatherHD() {
        try {
            Log.i("Made it:", "Weather Hourly/Daily");

            String url = getApplication().getResources().getString(R.string.base_url) + "weather/location/"+lat+"/"+lon;

            JSONObject body = new JSONObject();
            try {
                body.put("name", "7days");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Request request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    body,
                    this::handleResultHD,
                    this::handleErrorHD);

            request.setRetryPolicy(new DefaultRetryPolicy(
                    10_000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
            Log.i("7day works", "body: " + String.valueOf(body));
            Log.i("7day works", "response: " + String.valueOf(request));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getConnectWeatherCity(String city) {

        try {
            Log.i("Made it to getconnect", "connect city");
            String url = getApplication().getResources().getString(R.string.base_url) + "weather/?name=" + city;

            Request request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null, //no body for this get request
                    this::handleResultCity, this::handleErrorCity);
            request.setRetryPolicy(new DefaultRetryPolicy(10_000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Instantiate the RequestQueue and add the request to the queue
            Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
        }catch (Exception e){
            e.printStackTrace();
        }

    } //end of method

    public void getConnectWeatherZip(String zip) {

        try {
            Log.i("enter", "connect zip");
            String url = getApplication().getResources().getString(R.string.base_url) + "weather/zip/?code=" + zip;

            Request request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null, //no body for this get request
                    this::handleResultZip, this::handleErrorZip);
            request.setRetryPolicy(new DefaultRetryPolicy(10_000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Instantiate the RequestQueue and add the request to the queue
            Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

} // end of class





