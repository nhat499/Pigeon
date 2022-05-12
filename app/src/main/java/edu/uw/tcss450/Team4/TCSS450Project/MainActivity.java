package edu.uw.tcss450.Team4.TCSS450Project;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;


import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.ContactsRVAdapter;
import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.ContactsViewModel;

import edu.uw.tcss450.Team4.TCSS450Project.databinding.ActivityMainBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.NewMessageCountViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.services.PushReceiver;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatMessage;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatViewModel;

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
    private AppBarConfiguration mAppBarConfiguration;

    private MainPushMessageReceiver mPushMessageReceiver;

    private NewMessageCountViewModel mNewMessageModel;

    private ActivityMainBinding binding;



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
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getJwt(),args.getEmail())
        ).get(UserInfoViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_contacts, R.id.navigation_chat, R.id.navigation_weather)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // sets theme to dark mode on app open
        SharedPreferences settings = getSharedPreferences("settings", 0);
        boolean isChecked = settings.getBoolean("dark_mode", false);

        toggleDarkMode(isChecked);
        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_chat) {
                //When the user navigates to the chats page, reset the new message count.
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.
                mNewMessageModel.reset();
            }
        });
        mNewMessageModel.addMessageCountObserver(this, count -> {
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_chat);
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
        SharedPreferences settings = getSharedPreferences("settings", 0);
        boolean isChecked = settings.getBoolean("dark_mode", false);
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
            item.setChecked(!item.isChecked());
            SharedPreferences settings = getSharedPreferences("settings", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dark_mode", item.isChecked());
            editor.commit();
            toggleDarkMode(item.isChecked());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Toggles between dark mode and light mode
     *
     * @param isChecked is true when the menu item action_dark_mode is checked and false otherwise
     */
    private void toggleDarkMode(boolean isChecked) {
        if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }


    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
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
                if (nd.getId() != R.id.navigation_chat) {
                    mNewMessageModel.increment();
                }
                //Inform the view model holding chatroom messages of the new
                //message.
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);
            }
        }
    }

}
