package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddFromContactsBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddFromContactsCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.AddMemberFragmentArgs;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.AddMemberFragmentDirections;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.AddMemberViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.Contacts;
import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.ContactsViewModel;

public class AddFromContactsFragment extends Fragment {

    private ContactsViewModel mContacts;
    private FragmentAddFromContactsBinding mBinding;
    private UserInfoViewModel mUserModel;
    private AddMemberViewModel mAddMemberModel;
    private ChatViewModel mChatViewModel;
    private List<Contacts> mAddedContacts;
    private AddFromContactsFragmentArgs mArgs;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddFromContactsBinding.inflate(inflater);
        mArgs = AddFromContactsFragmentArgs.fromBundle(getArguments());
        return mBinding.getRoot();
    }

    public static void test() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContacts = new ViewModelProvider(getActivity()).get(ContactsViewModel.class);
        mUserModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mAddMemberModel = new ViewModelProvider(getActivity()).get(AddMemberViewModel.class);
        mChatViewModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        mContacts.getFirstContacts(mUserModel.getmJwt());
        mAddedContacts = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentAddFromContactsBinding binding = FragmentAddFromContactsBinding.bind(getView());
        // Sets title of action bar.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add From Contacts");
        mContacts.addContactObserver(getViewLifecycleOwner(), contacts -> {
            binding.listRoot.setAdapter(
                 new AddFromContactsRecyclerViewAdapter(mContacts.getContactListValue())
            );
        });

        mBinding.buttonDone.setOnClickListener(done ->
            attemptAddContacts(mChatViewModel.getCurrentRoom())
        );

        mAddMemberModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptAddContacts(int room) {
        if (mAddedContacts.size() > 0) {
            for (int i = 0; i < mAddedContacts.size(); i++) {
                Contacts current = mAddedContacts.get(i);
                mAddMemberModel.addMember(mUserModel.getmJwt(), current.getContactEmail(), room);
            }
        } else {
            mBinding.buttonDone.setError("Please select contact(s).");
            mBinding.errorView.setText("Please select contact(s).");
        }
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code") || response.has("error")) {
                try {
                    mBinding.buttonDone.setError(response.getJSONObject("data").getString("message"));
                    mBinding.errorView.setText("One or more users are already in the chat.");

                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                // This will work if at least one selected contact is not in the room.
                Navigation.findNavController(getView())
                        .navigate(AddFromContactsFragmentDirections
                                .actionAddFromContactsFragmentToAddMemberFragment(mArgs.getRoomName()));
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }



    // This needs to be inner class because we cannot access view model from separate recycler view adapter class.
    public class AddFromContactsRecyclerViewAdapter extends RecyclerView.Adapter<AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder> {

        private final Map<Contacts, Boolean> mExpandedFlags;

        private final List<Contacts> mContacts;

        public AddFromContactsRecyclerViewAdapter(@NonNull List<Contacts> contacts) {
            this.mContacts = contacts;
            mExpandedFlags = mContacts.stream()
                    .collect(Collectors.toMap(Function.identity(), blog -> false));
        }

        @NonNull
        @Override
        public AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.fragment_add_from_contacts_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder holder, int position) {
            holder.setContact(mContacts.get(position));
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }

        /**
         * Objects from this class represent an Individual row View from the List
         * of rows in the Blog Recycler View.
         */
        public class AddFromContactsViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public FragmentAddFromContactsCardBinding binding;
            private Contacts mSingleContact;

            public AddFromContactsViewHolder(View view) {
                super(view);
                mView = view;
                binding = FragmentAddFromContactsCardBinding.bind(view);
                AddFromContactsFragment.test();
                binding.checkBox.setOnClickListener(check ->
                {
                    if (binding.checkBox.isChecked()) {
                        mAddedContacts.add(mSingleContact);
                        Log.e("Checkbox Added : ", "" + mAddedContacts.toString());
                    } else {
                        mAddedContacts.remove(mSingleContact);
                        Log.e("Checkbox Remove : ", "" + mAddedContacts.toString());
                    }
                });
            }
            void setContact(final Contacts contact) {
                mSingleContact = contact;
                binding.textName.setText(contact.getFullName());
            }
        }
    }

}