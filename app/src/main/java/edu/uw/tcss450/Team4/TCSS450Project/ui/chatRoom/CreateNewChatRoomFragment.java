package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatRoomListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentCreateNewChatRoomBinding;

import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.AddMemberViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.signIn.SignInFragmentDirections;

public class CreateNewChatRoomFragment extends Fragment {

    private FragmentCreateNewChatRoomBinding mBinding;

    private UserInfoViewModel mUserViewModel;

    private CreateNewChatRoomViewModel mNewChatRoomModel;

    private AddMemberViewModel mAddMemberViewModel;

    private ChatRoomViewModel mChatRoomModel;

    public CreateNewChatRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserViewModel = provider.get(UserInfoViewModel.class);
        mNewChatRoomModel = provider.get(CreateNewChatRoomViewModel.class);
        mChatRoomModel = new ViewModelProvider(getActivity()).get(ChatRoomViewModel.class);
        mAddMemberViewModel = provider.get(AddMemberViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCreateNewChatRoomBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    // Initiate buttons and their bindings.
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sets title of action bar.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create Chat Room");

        // Retrieve chat room name from the text field.
        mBinding.buttonCreateNewChatRoom.setOnClickListener(button ->
                mNewChatRoomModel.createChatRoom(mUserViewModel.getmJwt(), mBinding.editChatRoomName.getText().toString()));

        // Attaches response observer to handle when we cannot create the room.
        mNewChatRoomModel.addResponseObserver(
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
            if (response.has("code")) {
                mBinding.editChatRoomName.setError("Please enter a name.");
            } else {
                try {
                    int chatID = response.getInt("chatID");
                    mAddMemberViewModel.addMember(mUserViewModel.getmJwt(), mUserViewModel.getEmail(), chatID);
                    // Go back to chat room list upon successful addition of chat room.
                    Navigation.findNavController(getView())
                            .navigate(CreateNewChatRoomFragmentDirections
                                    .actionCreateNewChatRoomFragmentToNavigationChatRoomList());
                    mNewChatRoomModel.clearResponse();
                    mChatRoomModel.getRooms(mUserViewModel.getmJwt(), mUserViewModel.getEmail());
                    mNewChatRoomModel.setChatHost(mUserViewModel.getmJwt(), chatID, mUserViewModel.getEmail());
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        }
    }
}
