package edu.uw.tcss450.Team4.TCSS450Project.ui.weather;

import android.app.Application;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentWeatherBinding;

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
    DecimalFormat df = new DecimalFormat("#.##");
    private FragmentWeatherBinding binding;


    // from root object of the weather data:
    private double lat;
    private double lon;
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
    }


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

    public void getConnectWeather() {
        try {
            Log.i("testing:", "Here");
            String url = "https://team-4-tcss-450-web-service.herokuapp.com/weather";

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

    public void getConnectWeatherHD() {
        try {
            Log.i("Made it:", "HERE TWO");

            String urlFiveDay = "https://team-4-tcss-450-web-service.herokuapp.com/weather";

            JSONObject body = new JSONObject();
            try {
                body.put("name", "5days");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Request request = new JsonObjectRequest(
                    Request.Method.GET,
                    urlFiveDay,
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


} // end of class





