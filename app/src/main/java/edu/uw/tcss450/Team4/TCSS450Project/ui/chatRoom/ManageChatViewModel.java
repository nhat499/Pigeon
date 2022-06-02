package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

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

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.io.RequestQueueSingleton;
import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.Contacts;

public class ManageChatViewModel  extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;
    private MutableLiveData<JSONObject> mCheckHostResponse;
    private MutableLiveData<List<Contacts>> mChatMembers;
    private boolean isHost;

    public ManageChatViewModel(@NonNull Application application) {
        super(application);
        isHost = false;
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mCheckHostResponse = new MutableLiveData<>();
        mCheckHostResponse.setValue(new JSONObject());
        mChatMembers = new MutableLiveData<>();
        mChatMembers.setValue(new ArrayList<>());
    }

    public void giveHostStatus() {
        isHost = true;
    }

    public void removeHostStatus() {
        isHost = false;
    }

    public boolean getHostStatus() {
        return isHost;
    }

    public List<Contacts> getChatMembersValue() {
        return mChatMembers.getValue();
    }

    public void removeChatMember(Contacts contact) {
        mChatMembers.getValue().remove(contact);
        mChatMembers.setValue(mChatMembers.getValue());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    public void addChatMembersResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<Contacts>> observer) {
        mChatMembers.observe(owner, observer);
    }

    public void addCheckHostResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mCheckHostResponse.observe(owner, observer);
    }

    public void getMembers(final String jwt, final int id) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "chats/getMembers";

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
                headers.put("chatid", String.valueOf(id));
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleSuccess(JSONObject response) {
        try {
            JSONArray members = response.getJSONArray("members");
            for (int i = 0; i < members.length(); i++) {
                Contacts contact = new Contacts(
                        String.valueOf(members.getJSONObject(i).get("firstname")),
                        String.valueOf(members.getJSONObject(i).get("lastname")),
                        String.valueOf(members.getJSONObject(i).get("email")),
                        Integer.valueOf((Integer) members.getJSONObject(i).get("memberid"))
                );
                if (!mChatMembers.getValue().contains(contact)) {
                    mChatMembers.getValue().add(contact);
                } else {
                    Log.wtf("Contact already received",
                            "Or duplicate id:" + contact.getMemberId());
                }
            }
            mChatMembers.setValue(mChatMembers.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setHost(final String jwt, final int id, final String email) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "chats/setHost";

        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                headers.put("chatid", String.valueOf(id));
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    public void remove(final String jwt, final String email, final int id) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "chats/" + id + "/" + email + "/";

        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                mResponse::setValue,
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
    }

    public void checkHost(final String jwt, final int id) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "chats/checkHost";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                mCheckHostResponse::setValue,
                this::handleCheckHostError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                headers.put("chatid", String.valueOf(id));
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                Log.d("TEST", error.getMessage());
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                Log.d("TEST", data);
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    private void handleCheckHostError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mCheckHostResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mCheckHostResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

}
