package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddFromContactsBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.AddMemberFragmentDirections;
import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.ContactsViewModel;

public class AddFromContactsFragment extends Fragment {

    private AddFromContactsViewModel mAddFromContacts;
    private ContactsViewModel mContacts;
    private FragmentAddFromContactsBinding mBinding;
    private UserInfoViewModel mUserModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddFromContactsBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddFromContacts = new ViewModelProvider(getActivity()).get(AddFromContactsViewModel.class);
        mContacts = new ViewModelProvider(getActivity()).get(ContactsViewModel.class);
        mUserModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mContacts.getFirstContacts(mUserModel.getmJwt());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentAddFromContactsBinding binding = FragmentAddFromContactsBinding.bind(getView());
        mContacts.addContactObserver(getViewLifecycleOwner(), contacts -> {
//            Log.e("Contact List", mContacts.getContactListValue().get(0).getUserFirstName());

            binding.listRoot.setAdapter(
                 new AddFromContactsRecyclerViewAdapter(mContacts.getContactListValue())
            );
        });
        mBinding.buttonDone.setOnClickListener(button ->
                Navigation.findNavController(getView())
                        .navigate(AddFromContactsFragmentDirections
                                .actionAddFromContactsFragmentToAddMemberFragment())
        );
    }
}