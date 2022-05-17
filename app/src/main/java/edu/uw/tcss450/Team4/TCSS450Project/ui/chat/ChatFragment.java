package edu.uw.tcss450.Team4.TCSS450Project.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.registration.RegistrationFragmentDirections;
import edu.uw.tcss450.Team4.TCSS450Project.ui.signIn.SignInFragmentArgs;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;

    //The chat ID for "global" chat
    private int HARD_CODED_CHAT_ID;

    private ChatViewModel mChatModel;
    private UserInfoViewModel mUserModel;
    private ChatSendViewModel mSendModel;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
        HARD_CODED_CHAT_ID = args.getRoom();
        Log.d("ASd", HARD_CODED_CHAT_ID + "");
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatModel = provider.get(ChatViewModel.class);
        mChatModel.getFirstMessages(HARD_CODED_CHAT_ID, mUserModel.getmJwt());
        mSendModel = provider.get(ChatSendViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatBinding binding = FragmentChatBinding.bind(getView());

        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

        final RecyclerView rv = binding.recyclerMessages;
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.
        rv.setAdapter(new ChatRecyclerViewAdapter(
                        mChatModel.getMessageListByChatId(HARD_CODED_CHAT_ID),
                        mUserModel.getEmail()));



        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatModel.getNextMessages(HARD_CODED_CHAT_ID, mUserModel.getmJwt());
        });

        // To pass along the chat room id to be saved so we can navigate back
        // to the original chat room.
        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());

        ChatFragmentDirections.ActionNavigationChatToAddMemberFragment directions =
                ChatFragmentDirections.actionNavigationChatToAddMemberFragment();

        directions.setRoom(args.getRoom());

        // Go to add new member fragment.
        binding.buttonAdd.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(directions));

        mChatModel.addMessageObserver(HARD_CODED_CHAT_ID, getViewLifecycleOwner(),
                list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    binding.swipeContainer.setRefreshing(false);
                });
//Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button -> {
            mSendModel.sendMessage(HARD_CODED_CHAT_ID,
                    mUserModel.getmJwt(),
                    binding.editMessage.getText().toString());
        });
//when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response ->
                binding.editMessage.setText(""));
    }
}
