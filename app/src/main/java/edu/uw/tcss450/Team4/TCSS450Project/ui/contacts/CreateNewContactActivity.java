package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.uw.tcss450.Team4.TCSS450Project.MainActivity;
import edu.uw.tcss450.Team4.TCSS450Project.R;

public class CreateNewContactActivity extends AppCompatActivity {
//
//    // creating a new variable for our edit text and button.
//    private EditText nameEdt, phoneEdt, emailEdt;
//    private Button addContactEdt;
//    private ContactsViewModel mContactsViewModel;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_add_contacts);
//
//
//        // on below line we are initializing our variables.
//        nameEdt = findViewById(R.id.edit2_contact_name);
//        phoneEdt = findViewById(R.id.edit2_contact_number);
//        emailEdt = findViewById(R.id.edit2_contact_email);
//        addContactEdt = findViewById(R.id.AddContact);
//
//        // on below line we are adding on click listener for our button.
//        addContactEdt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // on below line we are getting text from our edit text.
//                String name = nameEdt.getText().toString();
//                String phone = phoneEdt.getText().toString();
//                String email = emailEdt.getText().toString();
//
//                // on below line we are making a text validation.
//                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone)) {
//                    Toast.makeText(CreateNewContactActivity.this, "Please enter the data in all fields. ", Toast.LENGTH_SHORT).show();
//                } else {
//                    // calling a method to add contact.
//                    //mContactsViewModel.addContactObserver(HARD_CODED_CHAT_ID);
//                }
//            }
//        });
//
//    }
//
//    private void addContact(String name, String email, String phone) {
//        // in this method we are calling an intent and passing data to that
//        // intent for adding a new contact.
//        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
//        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
//        contactIntent
//                .putExtra(ContactsContract.Intents.Insert.NAME, name)
//                .putExtra(ContactsContract.Intents.Insert.PHONE, phone)
//                .putExtra(ContactsContract.Intents.Insert.EMAIL, email);
//        startActivityForResult(contactIntent, 1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // in on activity result method.
//        if (requestCode == 1) {
//            // we are checking if the request code is 1
//            if (resultCode == Activity.RESULT_OK) {
//                // if the result is ok we are displaying a toast message.
//                Toast.makeText(this, "Contact has been added.", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(CreateNewContactActivity.this, MainActivity.class);
//                startActivity(i);
//            }
//            // else we are displaying a message as contact addition has cancelled.
//            if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(this, "Cancelled Added Contact",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
