package edu.uw.tcss450.Team4.TCSS450Project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.DialogInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

//COMMENTED IMPORTS
/**
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
**/
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.Team4.TCSS450Project.databinding.ActivityMainBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatBinding;
import edu.uw.tcss450.Team4.TCSS450Project.io.RequestQueueSingleton;
import edu.uw.tcss450.Team4.TCSS450Project.model.NewMessageCountViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.services.PushReceiver;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatFragment;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatMessage;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.ChatRoomViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding.HomeLandingFragmentArgs;
import edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding.HomeLandingViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding.RecentMessageListViewModel;

/**
 * Class that defines the lifecycle for the Main Activity
 *
 * @author Team4
 * @version May 2022
 */
public class MainActivity extends AppCompatActivity {
    private String contactName = "n";
    private String contactNumber = "n";
    private TextView contactTV, nameTV;
    private ImageView contactIV, callIV, messageIV;
    private TextView user_email,user_uid;

    private MainActivityArgs args;

    private AppBarConfiguration mAppBarConfiguration;

    private MainPushMessageReceiver mPushMessageReceiver;

    private NewMessageCountViewModel mNewMessageModel;

    private ChatRoomViewModel mChatRoomModel;

    private ChatViewModel mChatModel;

    private ActivityMainBinding binding;

    private HomeLandingViewModel mHomeLanding;

    private MutableLiveData<JSONObject> mRemoveTokenResponse;

    private RecentMessageListViewModel mMessages;

    @Override
    protected void onStart(){
        super.onStart();

        // String email = mUser.gotEmail();
        //String current_user_uid = mUser.getUid();

        // user_email.setText(email);
        // user_uid.setText(current_user_uid);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextOfApplication = getApplicationContext();

        args = MainActivityArgs.fromBundle(getIntent().getExtras());

        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getJwt(),args.getEmail())
        ).get(UserInfoViewModel.class);

        // For removing the jwt on sign out.
        mRemoveTokenResponse = new MutableLiveData<>();
        mRemoveTokenResponse.setValue(new JSONObject());

        mChatRoomModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        mChatModel = new ViewModelProvider(this).get(ChatViewModel.class);
        mHomeLanding = new ViewModelProvider(this).get(HomeLandingViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        //Set up nav bar
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_contacts, R.id.navigation_chat_room_list, R.id.navigation_weather)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // sets theme to dark mode on app open
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        boolean isChecked = prefs.getBoolean(getString(R.string.keys_prefs_dark_mode), false);
        toggleDarkMode(isChecked);

        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_chat) {
                //When the user navigates to the chats page, reset the new message count.
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.
                mChatRoomModel.getNotificationList();
            }
        });

        // Observe current room changes. Clears notifications for the room that has been clicked on.
        mChatModel.addRoomObserver(this, count -> {
            if (!(mChatRoomModel.getNotificationList() == null)) {
                List<Integer> notifications = mChatRoomModel.getNotificationList();
                notifications.removeAll(Collections.singleton((mChatModel.getCurrentRoom())));
                int length = notifications.size();
                mNewMessageModel.setEqual(length);
            }
        });
        mMessages = new ViewModelProvider(this).get(RecentMessageListViewModel.class);


        mNewMessageModel.addMessageCountObserver(this, count -> {
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_chat_room_list);
            mMessages.connectGet(args.getEmail());

            badge.setMaxCharacterCount(2);
            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        getMenuInflater().inflate(R.menu.toolbar, menu);

        // updates the dark mode check box
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        boolean isChecked = prefs.getBoolean(getString(R.string.keys_prefs_dark_mode), false);
        MenuItem item = menu.findItem(R.id.action_dark_mode);
        item.setChecked(isChecked);

        MenuItem searchViewItem = menu.findItem(R.id.search);
        // on below line we are creating a variable for our search view.
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        // on below line we are setting on query text listener for our search view.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on query submit we are clearing the focus for our search view.
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // on changing the text in our search view we are calling
                // a filter method to filter our array list.
                //filter(newText.toLowerCase());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_dark_mode) {
            toggleDarkMode(item);
        } else if (id == R.id.action_change_password) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.changePasswordFragment);
        } else if (id == R.id.action_sign_out) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleDarkMode(MenuItem item) {
        item.setChecked(!item.isChecked());
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().putBoolean(getString(R.string.keys_prefs_dark_mode), item.isChecked()).apply();
        if (item.isChecked()) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void toggleDarkMode(boolean isChecked) {
        if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void signOut() {
        // Remove the pushy token on sign out.
        removeToken(args.getEmail());
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.authenticationActivity);
    }

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // below line is the title
        // for our alert dialog.
        builder.setTitle("Need Permissions");

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called on click on positive
                // button and on clicking shit button we
                // are redirecting our user from our app to the
                // settings page of our app.
                dialog.cancel();
                // below is the intent from which we
                // are redirecting our user.
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called when
                // user click on negative button.
                dialog.cancel();
            }
        });
        // below line is used
        // to display our dialog
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new MainPushMessageReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushMessageReceiver, iFilter);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReceiver != null){
            unregisterReceiver(mPushMessageReceiver);
        }
    }


    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushMessageReceiver extends BroadcastReceiver {
        private ChatViewModel mModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatViewModel.class);
        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();
            if (intent.hasExtra("chatMessage")) {
                ChatMessage cm = (ChatMessage) intent.getSerializableExtra("chatMessage");
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                if (nd.getId() != R.id.navigation_chat || (nd.getId() == R.id.navigation_chat &&
                        !(mChatModel.getCurrentRoom() == intent.getIntExtra("chatid", -1)))) {
                    mNewMessageModel.increment();
                }
                //Inform the view model holding chatroom messages of the new
                //message.
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);
                //Inform the chat room view model of notification of a specific room.
                mChatRoomModel.addNotification(intent.getIntExtra("chatid", -1));
                mHomeLanding.connect(args.getJwt(), args.getEmail());
            }
        }
    }

    public void removeToken(final String jwt) {
        Log.e("JWT: ", jwt);
        String url = getApplication().getResources().getString(R.string.base_url) +
                "auth/";
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null, //no body for this get request
                mRemoveTokenResponse::setValue,
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

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mRemoveTokenResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mRemoveTokenResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}