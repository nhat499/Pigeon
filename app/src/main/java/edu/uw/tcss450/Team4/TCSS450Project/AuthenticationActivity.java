package edu.uw.tcss450.Team4.TCSS450Project;
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
        SharedPreferences settings = getSharedPreferences("settings", 0);
        boolean isChecked = settings.getBoolean("dark_mode", false);
        toggleDarkMode(isChecked);
        Pushy.listen(this);
        initiatePushyTokenRequest();
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

    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }

}
