package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.app.Application;
import android.text.Editable;
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

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.io.RequestQueueSingleton;

public class ContactsViewModel extends AndroidViewModel {

    /* List of the currently signed in user's contacts */
    private MutableLiveData<List<Contacts>> mContacts;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mContacts = new MutableLiveData<>(new ArrayList<>());
    }

    public void addContactObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<Contacts>> observer) {
        mContacts.observe(owner, observer);
    }

    /**
     * Returns the contact list of the signed in user as MutableLiveData
     *
     * @return mContacts the list of the currently signed in user's contacts as MutableLiveData
     */
    private MutableLiveData<List<Contacts>> getContactList() {
        return mContacts;
    }

    /**
     * Returns the contact list of the signed in user as List<Contacts>
     *
     * @return mContacts.getValue() the list of currently signed in user's contacts as list
     */
    public List<Contacts> getContactListValue() {
        return getContactList().getValue();
    }

    /**
     * Get request to "https://team-4-tcss-450-web-service.herokuapp.com/contact"
     * to retrieve a list of contacts for the signed in user
     *
     * @param jwt the signed in users jwt
     */
    public void getFirstContacts(final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url)
                + "contact";

        Request request = new JsonArrayRequest(
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
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * Adds the contacts from the JSONArray to the mContacts list
     *
     * @param array JSONArray returned from the getFirstContacts get request
     */
    private void handleSuccess(final JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject response = null;

            try {
                response = array.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!response.has("memberid_a")) {
                throw new IllegalStateException("Unexpected response in ContactViewModel: " + response);
            }

            try {
                Contacts contact = new Contacts(
                        response.getString("firstname"),
                        response.getString("lastname"),
                        response.getString("email"),
                        response.getInt("memberid_b")
                );
                if (!mContacts.getValue().contains(contact)) {
                    mContacts.getValue().add(contact);
                } else {
                    Log.wtf("Contact already received",
                            "Or duplicate id:" + contact.getMemberId());
                }
            } catch (JSONException e) {
                Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
                Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
            }
        }
        mContacts.setValue(mContacts.getValue());
    }

    public void addContact(final String jwt, final Editable email) {
        String url = getApplication().getResources().getString(R.string.base_url)
                + "contact";

        JSONObject body = new JSONObject();
        try {
            body.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                this::handleAdd,
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
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleAdd(final JSONObject response) {
        if (!response.has("newContact") || !response.has("message")) {
            throw new IllegalStateException("Unexpected response in ContactViewModel: " + response);
        }
        try {
            JSONObject newContact = response.getJSONObject("newContact");
            Contacts contact = new Contacts(
                    newContact.getString("firstname"),
                    newContact.getString("lastname"),
                    newContact.getString("email"),
                    newContact.getInt("memberid")
            );
            if (!mContacts.getValue().contains(contact)) {
                mContacts.getValue().add(contact);
            } else {
                Log.wtf("Contact already received",
                        "Or duplicate id:" + contact.getMemberId());
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mContacts.setValue(mContacts.getValue());
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

    public void deleteContact(final String jwt, final String email) {
        String url = getApplication().getResources().getString(R.string.base_url)
                + "contact/delete";

        JSONObject body = new JSONObject();
        try {
            body.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                this::handleDelete,
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
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleDelete(final JSONObject response) {
        mContacts.getValue().remove(response);
    }
}
