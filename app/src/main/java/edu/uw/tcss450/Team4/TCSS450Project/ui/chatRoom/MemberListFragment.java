package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatRoomListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentMemberListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatFragmentArgs;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatViewModel;

public class MemberListFragment extends Fragment {

    private FragmentMemberListBinding mBinding;
    private UserInfoViewModel mUserModel;
    private ChatRoomViewModel mChatRoomModel;
    private ChatViewModel mChatModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMemberListBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatRoomModel = new ViewModelProvider(getActivity()).get(ChatRoomViewModel.class);
        mChatModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Users in Chat");

        FragmentMemberListBinding binding = FragmentMemberListBinding.bind(getView());

        MemberListFragmentArgs args = MemberListFragmentArgs.fromBundle(getArguments());

        mChatRoomModel.getMembersInChat(mUserModel.getmJwt(), mChatModel.getCurrentRoom());
        mChatRoomModel.addMemberListObserver(getViewLifecycleOwner(), Count -> {
            binding.listRoot.setAdapter(
                    new MemberListRecyclerViewAdapter(mChatRoomModel.getMemberList())
            );
            binding.textCount.setText("" + mChatRoomModel.getMemberList().size());
        });
    }
}