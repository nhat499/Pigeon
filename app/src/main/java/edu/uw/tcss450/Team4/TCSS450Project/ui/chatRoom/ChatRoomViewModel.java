package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.io.RequestQueueSingleton;


// USE THIS CLASS TO STORE CHAT ROOMS (AND USERS FOR EACH ROOM) ASSOCIATED WITH CURRENT USER.
public class ChatRoomViewModel extends AndroidViewModel {

    /**
     * A Map of Lists of Chat Rooms ids.
     */
    private MutableLiveData<List<ChatRoom>> mChatRoomList;
    private MutableLiveData<List<String>> mChatRoomName;

    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        mChatRoomList = new MutableLiveData<>(ChatRoomGenerator.getBlogList());
    }

    public void addChatRoomListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatRoom>> observer) {
        mChatRoomList.observe(owner, observer);
    }

    public List<ChatRoom> getChatList() {
        return mChatRoomList.getValue();
    }

    public List<ChatRoom> getChatRoomName(int chatID) {
        return null;
    }

    // Used when handling data from database.
    private void handleError(final VolleyError error) {
    }

    private void handleResult(final JSONObject result) {
    }

    // Get the list of chat room ids and chat room names HERE.
    public void connectGet() {
//        String url = getApplication().getResources().getString(R.string.base_url_service) +
//                "messages/" + chatId;
//
//        Request request = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null, //no body for this get request
//                this::handelSuccess,
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
//
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        //Instantiate the RequestQueue and add the request to the queue
//        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
//                .addToRequestQueue(request);
//
//        //code here will run
    }
}