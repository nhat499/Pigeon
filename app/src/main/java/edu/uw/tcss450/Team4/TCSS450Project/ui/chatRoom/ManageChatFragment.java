package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentManageChatBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentManageChatCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.AddMemberViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.Contacts;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageChatFragment extends Fragment {

    private FragmentManageChatBinding mBinding;
    private ManageChatViewModel mManageChatViewModel;
    private ChatViewModel mChatModel;
    private UserInfoViewModel mUserViewModel;
    private List<Contacts> mChatMembers;
    private AddMemberViewModel mAddViewModel;
    private Contacts mSelf;

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
        mAddViewModel = new ViewModelProvider(getActivity()).get(AddMemberViewModel.class);
        mChatMembers = new ArrayList<>();
        mManageChatViewModel.getMembers(mUserViewModel.getmJwt(), mChatModel.getCurrentRoom());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Manage Chat");
        mBinding.listRoot.setAdapter(new ManageChatRecyclerViewAdapter(mManageChatViewModel.getChatMembersValue()));
        mManageChatViewModel.addChatMembersResponseObserver(
                getViewLifecycleOwner(),
                list -> {
                    mBinding.listRoot.getAdapter().notifyDataSetChanged();
                    mBinding.listRoot.scrollToPosition(mBinding.listRoot.getAdapter().getItemCount() - 1);
                }
        );
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

            public FragmentManageChatCardBinding mBinding;

            public ManageChatViewHolder(View view) {
                super(view);

                mBinding = FragmentManageChatCardBinding.bind(view);
            }

            void setContact(final Contacts contact) {
                if (contact.getContactEmail().equals(mUserViewModel.getEmail())) {
                    mSelf = contact;
                }
                if (mSelf != null && mSelf == contact) {
                    mBinding.buttonRemove.setEnabled(false);
                    mBinding.buttonSetHost.setEnabled(false);
                } else {
                    mBinding.buttonRemove.setEnabled(true);
                    mBinding.buttonSetHost.setEnabled(true);
                }
                mBinding.textName.setText(contact.getFullName());
                mBinding.buttonRemove.setOnClickListener(button -> {
                    if (mManageChatViewModel.getHostStatus()) {
                        mManageChatViewModel.remove(mUserViewModel.getmJwt(), contact.getContactEmail(), mChatModel.getCurrentRoom());
                        mManageChatViewModel.removeChatMember(contact);
                    } else {
                        // only the host can remove
                    }
                });
                mBinding.buttonSetHost.setOnClickListener(button -> {
                    if (mManageChatViewModel.getHostStatus()) {
                        mManageChatViewModel.setHost(mUserViewModel.getmJwt(), mChatModel.getCurrentRoom(), contact.getContactEmail());
                        mManageChatViewModel.removeHostStatus();
                    } else {
                        // only the host can set host
                    }
                });
            }
        }
    }

}