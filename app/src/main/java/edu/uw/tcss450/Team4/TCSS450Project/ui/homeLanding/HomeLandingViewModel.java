package edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.io.RequestQueueSingleton;

/**
 * View model for the HomeLanding Fragment
 *
 * @author team4
 * @version May 2022
 */
public class HomeLandingViewModel extends AndroidViewModel {
    public MutableLiveData<HashMap<String, String>> mResponse;
    //public HashMap<String, String> userInfo = new HashMap<>();
    public MutableLiveData<ArrayList<HashMap<String, String>>> mResponseMessages;
    public HomeLandingViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>(new HashMap<String, String>());
        mResponseMessages = new MutableLiveData<>(new ArrayList<HashMap<String,String>>());
        //mResponse.setValue(new JSONObject());
    }

    public HashMap<String, String> getMResponse() {
        return mResponse.getValue();
    }

    public void HomeLandingObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super HashMap<String, String>> observer) {
        mResponse.observe(owner, observer);
    }

    public void connect(final String email, String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url_service) + "contact/" + email;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleSuccess,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
        //code here will run
    }

    public void getRecentMessages(String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url_service) + "messages";
        Request request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::storeRecentMessage,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
        //code here will run
    }

    private void storeRecentMessage(final JSONArray response) {
        ArrayList<HashMap<String, String>> storeInfo = new ArrayList<HashMap<String, String>>();
        Log.d("new test", "storeRecentMessage: " + response.toString());
        HashMap<String, String> userInfo = new HashMap<>();
        try{
            int length = response.length();
            for (int i = 0; i < length; i++) {
                 JSONObject obj = response.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("name", obj.getString("name"));
                map.put("message", obj.getString("message"));
                map.put("timestamp", obj.getString("timestamp"));
            }
        } catch (JSONException e) {

        }

    }



    private void handleSuccess(final JSONObject response) {
        HashMap<String, String> userInfo = new HashMap<>();
        try {
            String name = response.getJSONObject("userInfo").getString("firstname") + " " +
                          response.getJSONObject("userInfo").getString("lastname");
            String email = response.getJSONObject("userInfo").getString("email");
            userInfo.put("name", name);
            userInfo.put("email", email);

            String numOfContact = response.getString("numOfContact");
            String numOfMessages = response.getString("numOfMessages");
            userInfo.put("numOfContact", numOfContact);
            userInfo.put("numOfMessages", numOfMessages);
        } catch (JSONException e) {
            Log.e("handleSuccess", "handleSuccess: " + e );
        }
        mResponse.postValue(userInfo);

    }

    private void handleError(final VolleyError error) {

    }
}