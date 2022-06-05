package edu.uw.tcss450.Team4.TCSS450Project.ui.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.ChatRoomViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.CreateNewChatRoomViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.ManageChatViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding.HomeLandingViewModel;
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
    private CreateNewChatRoomViewModel mNewChatRoomModel;
    private ManageChatViewModel mManageChatViewModel;
    private UserInfoViewModel mUserInfoViewModel;
    private HomeLandingViewModel mHomeModel;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
        HARD_CODED_CHAT_ID = args.getRoom();
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatModel = provider.get(ChatViewModel.class);
        mChatModel.getFirstMessages(HARD_CODED_CHAT_ID, mUserModel.getmJwt());
        mSendModel = provider.get(ChatSendViewModel.class);
        mNewChatRoomModel = provider.get(CreateNewChatRoomViewModel.class);
        mManageChatViewModel = new ViewModelProvider(getActivity()).get(ManageChatViewModel.class);
        mUserInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mHomeModel = new ViewModelProvider(getActivity()).get(HomeLandingViewModel.class);
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

        mManageChatViewModel.checkHost(mUserInfoViewModel.getmJwt(), ChatFragmentArgs.fromBundle(getArguments()).getRoom());

        FragmentChatBinding binding = FragmentChatBinding.bind(getView());
        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

        final RecyclerView rv = binding.recyclerMessages;
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.

//        convertToName(mChatModel.getMessageListByChatId(HARD_CODED_CHAT_ID));


        rv.setAdapter(new ChatRecyclerViewAdapter(
                mHomeModel, mUserModel,
                        mChatModel.getMessageListByChatId(HARD_CODED_CHAT_ID),
                        mUserModel.getEmail()));

        mSendModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);

        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatModel.getNextMessages(HARD_CODED_CHAT_ID, mUserModel.getmJwt());
        });

        // To pass along the chat room id to be saved so we can navigate back
        // to the original chat room.
        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());

        ChatFragmentDirections.ActionNavigationChatToAddMemberFragment directions =
                ChatFragmentDirections.actionNavigationChatToAddMemberFragment(args.getRoom(), args.getRoomName());

        //directions.setRoom(args.getRoom());

        mManageChatViewModel.addCheckHostResponseObserver(
                getViewLifecycleOwner(),
                this::observeCheckHostResponse
        );


        // To prevent automatic navigation back to the list because of the HTTP response previously.
        binding.buttonAdd.setOnClickListener(button ->
                mNewChatRoomModel.clearResponse());

        // Go to add new member fragment.
        binding.buttonAdd.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(directions));


        // Go to member list.
        ChatFragmentDirections.ActionNavigationChatToMemberListFragment directions2 =
                ChatFragmentDirections.actionNavigationChatToMemberListFragment();
        binding.buttonMembers.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(directions2));



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

        binding.editMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.currCharacter.setText("" + binding.editMessage.getText().toString().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


//when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            Log.d("TAG", "onViewCreated: " + response.toString());
            try {
                if(response.getBoolean("success") == true) {
                    binding.editMessage.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        });

//        mChatModel.addCurrentCharacterObserver(getViewLifecycleOwner(), respond ->
//                binding.currCharacter.setText())
    }

//    private void convertToName(List<ChatMessage> messages) {
//        // Change each email in the message to their name.
//        for (int i = 0; i < messages.size(); i++) {
//            // Connect to get email.
//            Log.e("Original Name:", messages.get(i).getSender());
//            mHomeModel.connect(messages.get(i).getSender() + "", mUserModel.getmJwt());
//            // Get converted name from email.
//            HashMap<String, String> temp = mHomeModel.getMResponse();
//            Log.e("Name: ", temp.get("name"));
//            // Set email to name in chat message list.
//            messages.get(i).setSender(temp.get("name"));
//
//        }
//    }

    private void observeCheckHostResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                Log.d("TEST", "not host");
                mManageChatViewModel.removeHostStatus();
            } else if (response.has("error")) {
                Log.d("TEST", "error");
                // error
            } else {
                Log.d("TEST", "you are host");
                mManageChatViewModel.giveHostStatus();
            }
        } else {
            Log.d("JSON Response", "No Response");
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
            if (response.has("code")) {
                try {
                    binding.editMessage.setError(
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {

            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}
