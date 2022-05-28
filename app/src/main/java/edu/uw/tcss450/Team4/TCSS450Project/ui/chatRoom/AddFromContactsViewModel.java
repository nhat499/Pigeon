//package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;
//
//import android.app.Application;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Observer;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import edu.uw.tcss450.Team4.TCSS450Project.R;
//import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddFromContactsCardBinding;
//import edu.uw.tcss450.Team4.TCSS450Project.io.RequestQueueSingleton;
//import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.Contacts;
//
//public class AddFromContactsViewModel extends AndroidViewModel {
//
//    private MutableLiveData<JSONObject> mResponse;
//
//    private final List<Contacts> mAddedContacts;
//
//    public AddFromContactsViewModel(@NonNull Application application) {
//        super(application);
//        mResponse = new MutableLiveData<>();
//        mResponse.setValue(new JSONObject());
//        mAddedContacts = new ArrayList<Contacts>();
//    }
//
//    public void addResponseObserver(@NonNull LifecycleOwner owner,
//                                    @NonNull Observer<? super JSONObject> observer) {
//        mResponse.observe(owner, observer);
//    }
//
//    public void addContact(Contacts contact) {
//        mAddedContacts.add(contact);
//    }
//
//    public void removeContact(Contacts contact) {
//        mAddedContacts.remove(contact);
//    }
//
//    public List<Contacts> getAddedContacts() {
//        return mAddedContacts;
//    }
//
//    // Must call this method multiple times if we are trying to add many people
//    // to a chat room at once.
//    public void addMember(final String jwt, final String email, final int id) {
//        String url = getApplication().getResources().getString(R.string.base_url_service) +
//                "chats/" + id + "/" + email + "/";
//
//        Request request = new JsonObjectRequest(
//                Request.Method.PUT,
//                url,
//                null,
//                mResponse::setValue,
//                this::handleError) {
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                // add headers <key,value>
//                headers.put("Authorization", jwt);
//                return headers;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        //Instantiate the RequestQueue and add the request to the queue
//        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
//                .addToRequestQueue(request);
//
//    }
//
//    private void handleError(final VolleyError error) {
//        if (Objects.isNull(error.networkResponse)) {
//            try {
//                mResponse.setValue(new JSONObject("{" +
//                        "error:\"" + error.getMessage() +
//                        "\"}"));
//            } catch (JSONException e) {
//                Log.e("JSON PARSE", "JSON Parse Error in handleError");
//            }
//        } else {
//            String data = new String(error.networkResponse.data, Charset.defaultCharset());
//            try {
//                mResponse.setValue(new JSONObject("{" +
//                        "code:" + error.networkResponse.statusCode +
//                        ", data:" + data +
//                        "}"));
//            } catch (JSONException e) {
//                Log.e("JSON PARSE", "JSON Parse Error in handleError");
//            }
//        }
//    }
//
//    public void clearResponse() {
//        mResponse = new MutableLiveData<>();
//        mResponse.setValue(new JSONObject());
//    }
//}