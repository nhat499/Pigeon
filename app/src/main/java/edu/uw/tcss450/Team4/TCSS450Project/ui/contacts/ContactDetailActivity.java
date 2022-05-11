package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.uw.tcss450.Team4.TCSS450Project.R;

public class ContactDetailActivity extends AppCompatActivity {

    // creating variables for our image view and text view and string. .
    private String contactName, contactNumber;
    private TextView contactTV, nameTV;
    private ImageView contactIV, callIV, messageIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contacts_profile);

        // on below line we are getting data which
        // we passed in our adapter class with intent.
        contactName = getIntent().getStringExtra("name");
        contactNumber = getIntent().getStringExtra("contact");

        // initializing our views.
        nameTV = findViewById(R.id.profile_name);
        contactIV = findViewById(R.id.profile_image);
        contactTV = findViewById(R.id.profile_number);
        nameTV.setText(contactName);
        contactTV.setText(contactNumber);

        // on below line adding click listener for our calling image view.

    }


}

