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
    /**
     * A Map of Lists of Chat Messages.
     * The Key represents the Chat ID
     * The value represents the List of (known) messages for that that room.
     */
    private Map<Integer, MutableLiveData<List<Contacts>>> mContacts;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mContacts = new HashMap<>();
    }

    /**
     * Register as an observer to listen to a specific chat room's list of messages.
     * @param chatId the chatid of the chat to observer
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addContactObserver(int chatId,
                                   @NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<Contacts>> observer) {
        getOrCreateMapEntry(chatId).observe(owner, observer);
    }
    /**
     * Return a reference to the List<> associated with the chat room. If the View Model does
     * not have a mapping for this chatID, it will be created.
     *
     * WARNING: While this method returns a reference to a mutable list, it should not be
     * mutated externally in client code. Use public methods available in this class as
     * needed.
     *
     * @param memberId the id of the chat room List to retrieve
     * @return a reference to the list of messages
     */
    public List<Contacts> getContactsListByMemberId(final int memberId) {
        return getOrCreateMapEntry(memberId).getValue();
    }

    private MutableLiveData<List<Contacts>> getOrCreateMapEntry(final int memberId) {
        if(!mContacts.containsKey(memberId)) {
            mContacts.put(memberId, new MutableLiveData<>(new ArrayList<>()));
        }
        return mContacts.get(memberId);
    }
    /**
     * Makes a request to the web service to get the first batch of messages for a given Chat Room.
     * Parses the response and adds the ChatMessage object to the List associated with the
     * ChatRoom. Informs observers of the update.
     *
     * Subsequent requests to the web service for a given chat room should be made from
     * getNextMessages()
     *
     * @param jwt the users signed JWT
     */
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

        //code here will run
    }
    /**
     * When a chat message is received externally to this ViewModel, add it
     * with this method.
     * @param memberId
     * @param contact
     */
    public void addMessage(final int memberId, final Contacts contact) {
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
            if (!response.has("memberid_b")) {
                throw new IllegalStateException("Unexpected response in ContactViewModel: " + response);
            }
            try {
                list = getContactsListByMemberId(response.getInt("memberid_b"));
                JSONArray Contacts = response.getJSONArray("email");
                for (int i = 0; i < Contacts.length(); i++) {
                    JSONObject contact = Contacts.getJSONObject(i);
                    Contacts cContact = new Contacts(
                            contact.getString("firstname"),
                            contact.getString("lastname"),
                            contact.getString("email")
                    );
                    if (!list.contains(cContact)) {
                        // don't add a duplicate
                        list.add(0, cContact);
                    } else {
                        // this shouldn't happen but could with the asynchronous
                        // nature of the application
                        Log.wtf("Contact already received",
                                "Or duplicate id:" + cContact.getMessageId());
                    }

                }
                //inform observers of the change (setValue)
                getOrCreateMapEntry(response.getInt("chatId")).setValue(list);
            } catch (JSONException e) {
                Log.e("JSON PARSE ERROR", "Found in handle Success ChatViewModel");
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
