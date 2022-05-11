package edu.uw.tcss450.Team4.TCSS450Project.ui.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatBinding;


/**
 * Class to define the fragment lifecycle for the Chat Fragment
 *
 * @author team4
 * @version May 2022
 */
public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater);
        return binding.getRoot();
    }
}