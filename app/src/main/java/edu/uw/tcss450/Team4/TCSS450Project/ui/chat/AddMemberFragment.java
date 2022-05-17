package edu.uw.tcss450.Team4.TCSS450Project.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONObject;

import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddMemberBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentCreateNewChatRoomBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.CreateNewChatRoomFragmentDirections;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom.CreateNewChatRoomViewModel;

public class AddMemberFragment extends Fragment {

    private FragmentAddMemberBinding mBinding;

    private UserInfoViewModel mUserViewModel;

    private AddMemberViewModel mAddMemberViewModel;

    public AddMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserViewModel = provider.get(UserInfoViewModel.class);
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Member to Chat");

        // To navigate back to the same chat room.
        AddMemberFragmentArgs args = AddMemberFragmentArgs.fromBundle(getArguments());
        AddMemberFragmentDirections.ActionAddMemberFragmentToNavigationChat directions =
                AddMemberFragmentDirections.actionAddMemberFragmentToNavigationChat();
        directions.setRoom(args.getRoom());

        // Go back to chat room.
        mBinding.buttonAddMember.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(directions));

        // Attaches response observer to handle when we cannot create the room.
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
            if (response.has("code")) {
                mBinding.editMemberName.setError("Please enter a name.");
            } else {
                // Go back to chat room list upon successful addition of chat room.
//                Navigation.findNavController(getView())
//                        .navigate(AddMemberFragmentDirections
//                                .actionAddMemberFragmentToNavigationChat());
//                mAddMemberViewModel.clearResponse();
            }
        }
    }
}
