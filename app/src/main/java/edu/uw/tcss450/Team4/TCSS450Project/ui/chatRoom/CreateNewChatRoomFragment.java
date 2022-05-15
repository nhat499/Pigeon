package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.uw.tcss450.Team4.TCSS450Project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewChatRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewChatRoomFragment extends Fragment {

    private Button addChatRoom;

    public CreateNewChatRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNewChatRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNewChatRoomFragment newInstance(String param1, String param2) {
        CreateNewChatRoomFragment fragment = new CreateNewChatRoomFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_chat_room, container, false);
    }
}