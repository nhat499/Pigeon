package edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.Team4.TCSS450Project.io.RequestQueueSingleton;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.ChatRoom;

/**
 * View model for the HomeLanding Fragment
 *
 * @author team4
 * @version May 2022
 */
public class HomeLandingViewModel extends AndroidViewModel {
    public MutableLiveData<HashMap<String, String>> mResponse;
    //public HashMap<String, String> userInfo = new HashMap<>();

    public HomeLandingViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        //mResponse.setValue(new JSONObject());
    }

    public void connect(final String email, String jwt) {
        String url = "https://team-4-tcss-450-web-service.herokuapp.com/contact/" + email;
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
//nhat is gay and fat LOL kekw
        //code here will run
    }

    private void handleSuccess(final JSONObject response) {
        HashMap<String, String> userInfo = new HashMap<>();

        try {
            Log.d("test respond2", "onHandleSuccess: " +
                    response.toString());

            Log.d("test respond2", "onCreateView: " +
                    response.getJSONObject("userInfo").getString("firstname"));

            Log.d("test respond2", "onCreateView: " + "hello");

            String name = response.getJSONObject("userInfo").getString("firstname") + " " +
                          response.getJSONObject("userInfo").getString("lastname");
            String email = response.getJSONObject("userInfo").getString("email");
            userInfo.put("name", name);
            userInfo.put("email", email);

            String numOfContact = response.getString("numOfContact");
            String numOfMessages = response.getString("numOfMessages");
            userInfo.put("numOfContact", numOfContact);
            userInfo.put("numOfMessages", numOfMessages);
            Log.d("test", "handleSuccess: " + name + email + numOfContact + numOfMessages);

        } catch (JSONException e) {
            Log.e("handleSuccess", "handleSuccess: " + e );
        }
        mResponse.postValue(userInfo);
        //Log.d("test", "handleSuccess: "+ mResponse.toString());
    }

    private void handleError(final VolleyError error) {

    }
}