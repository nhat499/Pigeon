package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsProfileBinding;

public class ContactsProfile extends Fragment {

    // creating variables for our image view and text view and string. .
    private String contactName, contactEmail;
    private TextView contactTV, nameTV;
    private ImageView contactIV, callIV, messageIV;
    private ContactsViewModel mContactsViewModel;
    private FragmentContactsProfileBinding mBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentContactsProfileBinding.inflate(inflater);
        return mBinding.getRoot();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mContactsViewModel = provider.get(ContactsViewModel.class);
        // on below line we are getting data which
        // we passed in our adapter class with intent.

        //contactEmail = ;

        // initializing our views.


        // on below line adding click listener for our calling image view.

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactName = mContactsViewModel.getContactsName(0);
        Log.e("contact name: ", contactName);
       // contactEmail = mContactsViewModel.g

        mBinding.profileName.setText(contactName);
        mBinding.profileEmail.setText("");
    }
}
