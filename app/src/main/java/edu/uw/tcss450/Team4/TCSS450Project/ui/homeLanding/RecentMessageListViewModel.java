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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450.Team4.TCSS450Project.R;

public class RecentMessageListViewModel extends AndroidViewModel {
    private MutableLiveData<List<HashMap<String,String>>> mRecentMessageList;

    public RecentMessageListViewModel(@NonNull Application application) {
        super(application);
        mRecentMessageList = new MutableLiveData<>();
        mRecentMessageList.setValue(new ArrayList<HashMap<String,String>>());
    }

    public void addRecentMessageObserver(@NonNull LifecycleOwner owner,
                                         @NonNull Observer<? super List<HashMap<String,String>>> observer) {
        mRecentMessageList.observe(owner, observer);
    }

    public void connectGet(String jwt) {
        Log.d("TAG", "connectGet: " + jwt);
        String url =
                getApplication().getResources().getString(R.string.base_url_service) + "messages/recent/recent/recent";
        Request request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
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
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public List<HashMap<String, String>> getmRecentMessageList() {
        List<HashMap<String, String>> result =  new ArrayList<HashMap<String, String>>();
        if (!(mRecentMessageList.getValue()==null)) {
            return mRecentMessageList.getValue();
        }
        return  result;
    }

    public void handleResult(final JSONArray result) {
        List<HashMap<String,String>> store =  new ArrayList<HashMap<String,String>>();
        try {
            for (int i = 0; i < result.length(); i++) {
                HashMap<String, String> s = new HashMap<>();
                s.put("chatName", result.getJSONObject(i).getString("name"));
                s.put("message",result.getJSONObject(i).getString("message"));
                s.put("name", result.getJSONObject(i).getString("firstname"));
                s.put("time", result.getJSONObject(i).getString("timestamp"));
                s.put("chatId" , result.getJSONObject(i).getString("chatid"));
                store.add(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRecentMessageList.setValue(store);
        Log.d("TAG", "handleResult: " +result.toString());
    }

    public void handleError(final VolleyError error) {
        Log.d("TAG", "handleResult: Errrorororororor" );
    }
}
