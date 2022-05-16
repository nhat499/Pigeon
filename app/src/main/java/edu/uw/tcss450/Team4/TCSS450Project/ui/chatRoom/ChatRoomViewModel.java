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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;


// USE THIS CLASS TO STORE CHAT ROOMS (AND USERS FOR EACH ROOM) ASSOCIATED WITH CURRENT USER.
public class ChatRoomViewModel extends AndroidViewModel {

    /**
     * A Map of Lists of Chat Rooms
     * The Key represents the Chat ID
     * The value represents the List of (known) users for a chat room.
     */
    private MutableLiveData<List<ChatRoom>> mChatRoomList;

    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        mChatRoomList = new MutableLiveData<>();
        mChatRoomList.setValue(new ArrayList<>());
    }

    public void addChatRoomListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatRoom>> observer) {
        mChatRoomList.observe(owner, observer);
    }

    // Used when handling data from database.
    private void handleError(final VolleyError error) {
    }

    private void handleResult(final JSONObject result) {
    }

    public void connectGet() {
    }
}