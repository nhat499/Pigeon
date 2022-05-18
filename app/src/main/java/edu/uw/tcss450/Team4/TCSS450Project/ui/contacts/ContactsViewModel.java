package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.Team4.TCSS450Project.io.RequestQueueSingleton;

public class ContactsViewModel extends AndroidViewModel {

    private Map<Integer, MutableLiveData<List<Contacts>>> mContacts;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mContacts = new HashMap<>();
    }


    public void addContactObserver(int memberId,
                                   @NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<Contacts>> observer) {
        getOrCreateMapEntry(memberId).observe(owner, observer);
    }

    public List<Contacts> getContactsListByMemberId(final int memberId) {
        return getOrCreateMapEntry(memberId).getValue();
    }

    public String getContactsName(final int memberId) {
        String name;
        List<Contacts> result = getOrCreateMapEntry(memberId).getValue();
       return result.get(memberId).toString();
    }

    private MutableLiveData<List<Contacts>> getOrCreateMapEntry(final int memberId) {
        if(!mContacts.containsKey(memberId)) {
            mContacts.put(memberId, new MutableLiveData<>(new ArrayList<>()));
        }
        return mContacts.get(memberId);
    }

    public void getFirstContacts(final String jwt) {
        String url = "https://team-4-tcss-450-web-service.herokuapp.com/"+
                "Contact";

        Request request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handelSuccess,
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

    public void addContact(final int memberId, final Contacts contact) {
        List<Contacts> list = getContactsListByMemberId(memberId);
        list.add(contact);
        getOrCreateMapEntry(memberId).setValue(list);
    }

    private void handelSuccess(final JSONArray array) {
        for(int j=0; j < array.length(); j++) {

            JSONObject response = null;
            try {
                response = array.getJSONObject(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<Contacts> list;
            if (!response.has("memberid_a")) {
                throw new IllegalStateException("Unexpected response in ContactViewModel: " + response);
            }
            try {
                list = getContactsListByMemberId(response.getInt("memberid_a"));
                //JSONArray contacts = response.getJSONArray("email");
                //for (int i = 0; i < response.length(); i++) {
                Contacts contact = new Contacts(
                        response.getString("firstname"),
                        response.getString("lastname"),
                        response.getString("email")
                );
                Log.d("CONTACTS", response.getString("firstname"));
                Log.d("CONTACTS", response.getString("lastname"));
                Log.d("CONTACTS", response.getString("email"));
                contact.setMemberId(j);
                if (!list.contains(contact)) {
                    // don't add a duplicate
                    list.add(contact.getMemberId(), contact);
                } else {
                    // this shouldn't happen but could with the asynchronous
                    // nature of the application
                    Log.wtf("Contact already received",
                            "Or duplicate id:" + contact.getMemberId());
                }

                //}
                //inform observers of the change (setValue)
                getOrCreateMapEntry(response.getInt("memberid_a")).setValue(list);
            } catch (JSONException e) {
                Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
                Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
            }
        }
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }
}
