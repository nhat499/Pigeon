package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddFromContactsBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddFromContactsCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentManageChatBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentManageChatCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.AddMemberViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.Contacts;
import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.ContactsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageChatFragment extends Fragment {

    private FragmentManageChatBinding mBinding;
    private ManageChatViewModel mManageChatViewModel;
    private ChatViewModel mChatModel;
    private UserInfoViewModel mUserViewModel;
    private List<Contacts> mChatMembers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentManageChatBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManageChatViewModel = new ViewModelProvider(getActivity()).get(ManageChatViewModel.class);
        mChatModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mChatMembers = new ArrayList<>();
        mManageChatViewModel.getMembers(mUserViewModel.getmJwt(), mChatModel.getCurrentRoom());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Manage Chat");
        mManageChatViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse
        );
    }

    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code") || response.has("error")) {
                try {
                    Log.d("TEST", String.valueOf(response.getBoolean("success")));
                    //mBinding.buttonDone.setError(response.getJSONObject("data").getString("message"));
                    //mBinding.errorView.setText("One or more users are already in the chat.");

                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    JSONArray members = response.getJSONArray("members");
                    for (int i = 0; i < members.length(); i++) {
                        mChatMembers.add(new Contacts(
                                String.valueOf(members.getJSONObject(i).get("firstname")),
                                String.valueOf(members.getJSONObject(i).get("lastname")),
                                String.valueOf(members.getJSONObject(i).get("email")),
                                Integer.valueOf((Integer) members.getJSONObject(i).get("memberid"))
                        ));
                    }
                    mBinding.listRoot.setAdapter(
                            new ManageChatRecyclerViewAdapter(mChatMembers)
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    public class ManageChatRecyclerViewAdapter extends RecyclerView.Adapter<ManageChatRecyclerViewAdapter.ManageChatViewHolder> {

        private final List<Contacts> mContacts;

        public ManageChatRecyclerViewAdapter(@NonNull List<Contacts> contacts) {
            this.mContacts = contacts;
        }

        @NonNull
        @Override
        public ManageChatRecyclerViewAdapter.ManageChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ManageChatRecyclerViewAdapter.ManageChatViewHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.fragment_manage_chat_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ManageChatFragment.ManageChatRecyclerViewAdapter.ManageChatViewHolder holder, int position) {
            holder.setContact(mContacts.get(position));
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }

        public class ManageChatViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public FragmentManageChatCardBinding mBinding;
            private Contacts mSingleContact;

            public ManageChatViewHolder(View view) {
                super(view);
                mView = view;
                mBinding = FragmentManageChatCardBinding.bind(view);
            }
            void setContact(final Contacts contact) {
                mSingleContact = contact;
                mBinding.textName.setText(contact.getFullName());
            }
        }
    }

}