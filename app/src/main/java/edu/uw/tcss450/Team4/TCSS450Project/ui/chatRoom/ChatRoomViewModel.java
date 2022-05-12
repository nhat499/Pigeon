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

public class ChatRoomViewModel extends AndroidViewModel {
    private MutableLiveData<List<ChatRoom>> mBlogList;

    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        mBlogList = new MutableLiveData<>();
        mBlogList.setValue(new ArrayList<>());
    }

    public void addChatRoomListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatRoom>> observer) {
        mBlogList.observe(owner, observer);
    }

    // Used when handling data from database.
    private void handleError(final VolleyError error) {
    }

    private void handleResult(final JSONObject result) {
    }

    public void connectGet() {
    }
}