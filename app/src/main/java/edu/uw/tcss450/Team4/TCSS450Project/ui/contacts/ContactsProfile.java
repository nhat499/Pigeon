package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsProfileBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;

public class ContactsProfile extends Fragment {

    private ContactsViewModel mContactsViewModel;
    private FragmentContactsProfileBinding mBinding;
    private Bundle mArgs;
    private UserInfoViewModel mUserModel;
    private Button profileDelete;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentContactsProfileBinding.inflate(inflater);
        mArgs = getArguments();
        return mBinding.getRoot();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mContactsViewModel = provider.get(ContactsViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        profileDelete = mBinding.buttonProfileDelete;
//        // on below line we are adding on click listener for our button.
//        profileDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("test respond", "onClick: " + mBinding.profileEmail.toString() );
//                mContactsViewModel.deleteContact(mUserModel.getmJwt(),"shirwaa331@gmail.com");
//
//                Navigation.findNavController(v).navigate(
//                        ContactsProfileDirections.actionContactsProfileToNavigationContacts2());
//            }
//        });
       //contactName = mContactsViewModel.getContactsName(133);
        //Log.e("contact name: ", contactName);
       // contactEmail = mContactsViewModel.g

        mBinding.profileName.setText(mArgs.getString("name"));
        mBinding.profileEmail.setText(mArgs.getString("email"));
    }
}
