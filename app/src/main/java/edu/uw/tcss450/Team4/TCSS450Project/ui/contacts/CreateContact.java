package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddContactsBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;

public class CreateContact extends Fragment {

    private ContactsViewModel mContactsViewModel;

    private UserInfoViewModel mUserModel;

    private FragmentAddContactsBinding mBinding;

    private Button addContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddContactsBinding.inflate(inflater);
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
        addContact = mBinding.AddContact;
        // on below line we are adding on click listener for our button.
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable name = mBinding.edit2ContactName.getText();
                Editable email = mBinding.edit2ContactEmail.getText();

                // on below line we are making a text validation.
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Please enter the data in all fields. ", Toast.LENGTH_SHORT).show();
                }
                else {
                    //mContactsViewModel.addContact(mUserModel.getmJwt(),mBinding.edit2ContactEmail.getText());
                    Navigation.findNavController(v).navigate(
                            CreateContactDirections.actionCreateContactToNavigationContacts());
                }

            }
        });
    }

}
