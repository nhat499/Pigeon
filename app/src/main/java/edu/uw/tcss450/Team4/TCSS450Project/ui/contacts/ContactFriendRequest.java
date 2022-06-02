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

import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddContactsBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactFriendrequestCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;

public class ContactFriendRequest extends Fragment{

        private ContactsViewModel mContactsViewModel;

        private UserInfoViewModel mUserModel;

        private FragmentContactFriendrequestCardBinding mBinding;
        private Bundle mArgs;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mBinding = FragmentContactFriendrequestCardBinding.inflate(inflater);
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


    }

}
