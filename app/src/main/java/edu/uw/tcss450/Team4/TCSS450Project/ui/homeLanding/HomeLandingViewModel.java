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
    public MutableLiveData<JSONObject> mResponse;

    public HomeLandingViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
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

        //code here will run
    }

    private void handleSuccess(final JSONObject response) {
        //List<ChatRoom> list = new ArrayList<ChatRoom>();
//        try {
//            JSONArray rooms = response.getJSONArray("rooms");
//
//        } catch (JSONException e) {
//            Log.e("JSON PARSE ERROR", "Found in handle Success ChatViewModel");
//            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
//        }
        mResponse.setValue(response);
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
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}