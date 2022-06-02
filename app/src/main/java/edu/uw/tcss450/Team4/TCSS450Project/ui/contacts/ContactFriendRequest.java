package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddContactsBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactFriendrequestCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactSendrequestBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;

public class ContactFriendRequest extends Fragment{

        private ContactsViewModel mContactsViewModel;
        private ContactsFriendRequestRVAdapter contactsAdapter;
        private UserInfoViewModel mUserModel;
        private FragmentContactSendrequestBinding mBinding;
        private Bundle mArgs;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mBinding = FragmentContactSendrequestBinding.inflate(inflater);
            mArgs = getArguments();
            return mBinding.getRoot();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ViewModelProvider provider = new ViewModelProvider(getActivity());
            mUserModel = provider.get(UserInfoViewModel.class);
            mContactsViewModel = provider.get(ContactsViewModel.class);
            mContactsViewModel.getFirstContacts(mUserModel.getmJwt());
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

            super.onViewCreated(view, savedInstanceState);
            final RecyclerView rv = mBinding.friendRequestRv;
            rv.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(layoutManager);
            contactsAdapter = new ContactsFriendRequestRVAdapter((ArrayList<Contacts>) mContactsViewModel.getContactListValue());
            rv.setAdapter(contactsAdapter);
    }

}
