package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

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

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatRoomListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentRegistrationBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding.HomeLandingViewModel;

public class ChatRoomListFragment extends Fragment {

    private FragmentChatRoomListBinding mBinding;
    private UserInfoViewModel mUserModel;
    private ChatRoomViewModel mChatRoomModel;
    private ChatViewModel mChatModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentChatRoomListBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel .class);
        mChatRoomModel = new ViewModelProvider(getActivity()).get(ChatRoomViewModel.class);
        mChatModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        mChatRoomModel.getRooms(mUserModel.getmJwt(), mUserModel.getEmail());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentChatRoomListBinding binding = FragmentChatRoomListBinding.bind(getView());
        mChatModel.getFirstMessages(1, mUserModel.getmJwt());
        mChatRoomModel.addChatRoomListObserver(getViewLifecycleOwner(), ChatRoomList -> {
            binding.listRoot.setAdapter(
                        new ChatRoomRecyclerViewAdapter(mUserModel, mChatModel, mChatRoomModel.getChatList(), mChatRoomModel.getNotificationList())
                );
        });

        // Upon going back to the fragment, this fixes the bug of user seeing notifications for their own messages.
        mChatRoomModel.clearNotificationForRoom(mChatModel.getCurrentRoom());
        mChatModel.setCurrentRoom(-1);
        mChatRoomModel.clearNotificationForRoom(mChatModel.getCurrentRoom());

        mChatRoomModel.addNotificationObserver(getViewLifecycleOwner(), ChatRoomList -> {
                    binding.listRoot.setAdapter(
                            new ChatRoomRecyclerViewAdapter(mUserModel, mChatModel, mChatRoomModel.getChatList(), mChatRoomModel.getNotificationList())
                    );
                });
        mBinding.buttonToCreateChatRoom.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ChatRoomListFragmentDirections.actionNavigationChatRoomListToCreateNewChatRoomFragment()
                ));
    }
}