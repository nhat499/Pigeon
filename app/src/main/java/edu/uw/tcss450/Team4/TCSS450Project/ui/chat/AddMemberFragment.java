package edu.uw.tcss450.Team4.TCSS450Project.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddMemberBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentCreateNewChatRoomBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.ChatRoomViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.CreateNewChatRoomFragmentDirections;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.CreateNewChatRoomViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.ManageChatViewModel;

public class AddMemberFragment extends Fragment {

    private FragmentAddMemberBinding mBinding;

    private UserInfoViewModel mUserViewModel;

    private AddMemberViewModel mAddMemberViewModel;

    private ChatRoomViewModel mChatRoomModel;

    private ManageChatViewModel mManageChatViewModel;


    public AddMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserViewModel = provider.get(UserInfoViewModel.class);
        mChatRoomModel = new ViewModelProvider(getActivity()).get(ChatRoomViewModel.class);
        mManageChatViewModel = provider.get(ManageChatViewModel.class);
        mAddMemberViewModel = provider.get(AddMemberViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddMemberBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    // Initiate buttons and their bindings.
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Sets title of action bar.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chat Settings");

        // To navigate back to the same chat room.
        AddMemberFragmentArgs args = AddMemberFragmentArgs.fromBundle(getArguments());
        AddMemberFragmentDirections.ActionAddMemberFragmentToNavigationChat directions =
                AddMemberFragmentDirections.actionAddMemberFragmentToNavigationChat(args.getRoomName());
        directions.setRoom(args.getRoom());
        // To navigate to add from contacts.
        AddMemberFragmentDirections.ActionAddMemberFragmentToAddFromContactsFragment directionsContacts =
                AddMemberFragmentDirections.actionAddMemberFragmentToAddFromContactsFragment(args.getRoomName());
        directionsContacts.setRoom(args.getRoom());

        if (!mManageChatViewModel.getHostStatus()) {
            mBinding.buttonAddMember.setEnabled(false);
            mBinding.buttonAddFromContacts.setEnabled(false);
            mBinding.editMemberName.setEnabled(false);
            mBinding.textMessage.setText("You must be the host of the chat to add/remove members or delete the chat.");
            mBinding.buttonManageChat.setEnabled(false);
            mBinding.buttonDeleteChat.setEnabled(false);
        } else {
            mBinding.textMessage.setText("");
            mBinding.buttonRemoveYourself.setEnabled(false);
        }


        mBinding.textChatName.setText(args.getRoomName());

        // Go back to chat room.
        mBinding.buttonAddMember.setOnClickListener(button ->
                mAddMemberViewModel.addMember(mUserViewModel.getmJwt(), mBinding.editMemberName.getText().toString().trim(), args.getRoom()));

        mBinding.buttonRemoveYourself.setOnClickListener(button ->
                mAddMemberViewModel.remove(mUserViewModel.getmJwt(), mUserViewModel.getEmail(), args.getRoom()));

        mBinding.buttonAddFromContacts.setOnClickListener(button ->
                Navigation.findNavController(getView())
                    .navigate(AddMemberFragmentDirections
                        .actionAddMemberFragmentToAddFromContactsFragment(args.getRoomName()))
                );

        mBinding.buttonManageChat.setOnClickListener(button ->
                Navigation.findNavController(getView())
                    .navigate(AddMemberFragmentDirections
                        .actionAddMemberFragmentToManageChatFragment())
                );

        mBinding.buttonDeleteChat.setOnClickListener(button ->
                mAddMemberViewModel.deleteChat(mUserViewModel.getmJwt(), args.getRoom())
                );
        mAddMemberViewModel.addDeleteRoomResponseObserver(
                getViewLifecycleOwner(),
                this::observeDeleteRoomResponse
        );
        mAddMemberViewModel.clearResponse();
        mAddMemberViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);
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
                    mBinding.editMemberName.setError(response.getJSONObject("data").getString("message"));

                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                Log.d("HELLO", "w");
                Navigation.findNavController(getView())
                        .navigate(AddMemberFragmentDirections
                                .actionAddMemberFragmentToNavigationChatRoomList());
                mChatRoomModel.getRooms(mUserViewModel.getmJwt(), mUserViewModel.getEmail());
                mAddMemberViewModel.clearResponse();
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
    private void observeDeleteRoomResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code") || response.has("error")) {
                try {
                    mBinding.editMemberName.setError(response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                Navigation.findNavController(getView())
                        .navigate(AddMemberFragmentDirections
                                .actionAddMemberFragmentToNavigationChatRoomList());
                mAddMemberViewModel.clearDeleteRoomResponse();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
