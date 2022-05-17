package edu.uw.tcss450.Team4.TCSS450Project;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.Team4.TCSS450Project.model.PushyTokenViewModel;
import me.pushy.sdk.Pushy;

/**
 * Class that defines the lifecycle for the Authentication activity
 *
 * @author Team4
 * @version May 2022
 */
public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Pushy.listen(this);
        initiatePushyTokenRequest();
        //Toggles dark mode on create
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        boolean isChecked = prefs.getBoolean(getString(R.string.keys_prefs_dark_mode), false);
        toggleDarkMode(isChecked);
    }

    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
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
}
