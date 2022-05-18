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

import java.lang.reflect.Array;
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
    private MutableLiveData<List<Integer>> mChatRoomNotifications;

    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        mChatRoomList = new MutableLiveData<>();
        mChatRoomNotifications = new MutableLiveData<>();
        Log.e("ASDADDSA", "CHATROOMVIEWMODEL INSTATIATION");
    }

    public void addChatRoomListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatRoom>> observer) {
        mChatRoomList.observe(owner, observer);
    }

    public void addNotificationObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super List<Integer>> observer) {
        mChatRoomNotifications.observe(owner, observer);
    }

    public List<ChatRoom> getChatList() {
        List<ChatRoom> result = new ArrayList<ChatRoom>();
        if (!(mChatRoomList.getValue() == null)) {
            result = mChatRoomList.getValue();
        }
        return result;
    }

    public List<Integer> getNotificationList() {
        List<Integer> result = new ArrayList<Integer>();
        if (!(mChatRoomNotifications.getValue() == null)) {
            result = mChatRoomNotifications.getValue();
        }
        return result;
    }

    // No "add" method directly to mutablelivedata, this is a workaround to add an element.
    public void addNotification(int i) {
        List<Integer> result = new ArrayList<Integer>();

        // If there are no notifications, it handles it here.
        if (mChatRoomNotifications.getValue() == null) {
            result.add(i);
        } else {
            result = mChatRoomNotifications.getValue();
            result.add(i);
        }
        mChatRoomNotifications.setValue(result);
    }

    // Get the list of chat room ids and chat room names HERE.
    public void getRooms(final String jwt, final String email) {
        String url = getApplication().getResources().getString(R.string.base_url_service) +
                "chats/getRooms";

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
                headers.put("email", email);
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

    // Used when handling data from database.
    private void handleError(final VolleyError error) {

    }

    // Adds the rooms to the fragment.
    private void handleSuccess(final JSONObject response) {
        List<ChatRoom> list = new ArrayList<ChatRoom>();
        try {
            JSONArray rooms = response.getJSONArray("rooms");
            for (int i = 0; i < rooms.length(); i++) {
                JSONObject objectRoom = rooms.getJSONObject(i);
                ChatRoom room = new ChatRoom.Builder(objectRoom.getString("name"),
                        objectRoom.getInt("chatid")).build();
                if (!list.contains(room)) {
                    // don't add a duplicate
                    list.add(0, room);
                } else {
                    // this shouldn't happen but could with the asynchronous
                    // nature of the application
                    Log.wtf("Chat message already received",
                            "Or duplicate id:" + room.getTitle());
                }
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mChatRoomList.setValue(list);
    }
}