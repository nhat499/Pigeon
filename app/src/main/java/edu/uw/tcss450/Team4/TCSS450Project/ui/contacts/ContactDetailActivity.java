package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.Team4.TCSS450Project.MainActivity;
import edu.uw.tcss450.Team4.TCSS450Project.R;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.uw.tcss450.Team4.TCSS450Project.MainActivity;
import edu.uw.tcss450.Team4.TCSS450Project.R;

public class ContactDetailActivity extends AppCompatActivity {

    // creating variables for our image view and text view and string. .
    private String contactName, contactEmail;
    private TextView contactTV, nameTV;
    private ImageView contactIV, callIV, messageIV;
    private ContactsViewModel mContactsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contacts_profile);
        ViewModelProvider provider = new ViewModelProvider(new ContactDetailActivity());
        mContactsViewModel = provider.get(ContactsViewModel.class);
        // on below line we are getting data which
        // we passed in our adapter class with intent.
        contactName = mContactsViewModel.getContactsName(3);
        Log.e("contact name: ", contactName);
        //contactEmail = ;

        // initializing our views.
        nameTV = findViewById(R.id.profile_name);
        contactIV = findViewById(R.id.profile_image);
        contactTV = findViewById(R.id.profile_email);
        nameTV.setText(contactName);
        contactTV.setText(contactEmail);

        // on below line adding click listener for our calling image view.

    }


}

