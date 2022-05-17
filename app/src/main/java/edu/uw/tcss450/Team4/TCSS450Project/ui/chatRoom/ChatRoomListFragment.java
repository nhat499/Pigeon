package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatRoomListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentRegistrationBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;

public class ChatRoomListFragment extends Fragment {

    private FragmentChatRoomListBinding mBinding;
    private UserInfoViewModel mUserModel;
    private ChatRoomViewModel mChatRoomModel;

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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentChatRoomListBinding binding = FragmentChatRoomListBinding.bind(getView());

        mChatRoomModel.addChatRoomListObserver(getViewLifecycleOwner(), ChatRoomList -> {
                binding.listRoot.setAdapter(
                        new ChatRoomRecyclerViewAdapter(mChatRoomModel.getChatList())
                );
        });
        mBinding.buttonToCreateChatRoom.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ChatRoomListFragmentDirections.actionNavigationChatRoomListToCreateNewChatRoomFragment()
                ));
    }

}