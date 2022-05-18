package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;

/**
 * Class to define the fragment lifecycle for the Contacts Fragment
 *
 * @author team4
 * @version May 2022
 */
public class ContactsFragment extends Fragment {
    private FragmentContactsListBinding binding;
    private UserInfoViewModel mUserModel;
    private ContactsViewModel mContactsViewModel;

    public ContactsFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mContactsViewModel = provider.get(ContactsViewModel.class);
        mContactsViewModel.getFirstContacts(mUserModel.getmJwt());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //On button click, navigate to registration
        FragmentContactsListBinding binding = FragmentContactsListBinding.bind(getView());

        final RecyclerView rv = binding.listRoot;

        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new ContactsRVAdapter(
                getActivity(), (ArrayList<Contacts>) mContactsViewModel.getContactsListByMemberId(133)));
        binding.buttonToCreateContact.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ContactsFragmentDirections.actionNavigationContactsToCreateContact()
                ));

        mContactsViewModel.addContactObserver(133, getViewLifecycleOwner(),
                list -> {
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                });


    }

}
