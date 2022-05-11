package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentSignInBinding;
import edu.uw.tcss450.Team4.TCSS450Project.ui.registration.EmailVerificationFragmentDirections;
import edu.uw.tcss450.Team4.TCSS450Project.ui.signIn.SignInFragmentArgs;
import edu.uw.tcss450.Team4.TCSS450Project.ui.signIn.SignInFragmentDirections;

/**
 * Class to define the fragment lifecycle for the Contacts Fragment
 *
 * @author team4
 * @version May 2022
 */
public class ContactsFragment extends Fragment {
    private FragmentContactsListBinding binding;
    private ContactsListViewModel mModel;
    private String contactName, contactNumber;
    private TextView contactTV, nameTV;
    private ImageView contactIV, callIV, messageIV;
    private ArrayList<ContactsViewModel> contactsModalArrayList;
    private RecyclerView contactRV;
    private ContactsRVAdapter contactRVAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts_list, container, false);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ContactsListViewModel.class);



    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //On button click, navigate to registration
        FragmentContactsListBinding binding = FragmentContactsListBinding.bind(getView());



        binding.buttonToCreateContact.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ContactsFragmentDirections.actionNavigationContactsToCreateNewContactActivity()
                ));


    }

}