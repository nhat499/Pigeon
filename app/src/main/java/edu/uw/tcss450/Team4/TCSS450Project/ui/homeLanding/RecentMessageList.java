package edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentRecentMessageListBinding;

/**
 * create an instance of this fragment.
 */
public class RecentMessageList extends Fragment {
    private RecentMessageListViewModel mModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(RecentMessageListViewModel.class);
        mModel.connectGet("sadasd");
        // call api for informations here
        //mModel.connection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView: We r infalting");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_message_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG", "onViewCreated: " + mModel.getmRecentMessageList());
        FragmentRecentMessageListBinding binding = FragmentRecentMessageListBinding.bind((getView()));
        mModel.addRecentMessageObserver(getViewLifecycleOwner(), messageList -> {
            Log.d("TAG", "onViewCreated: " + messageList);
            if(!messageList.isEmpty()) {
//                binding.listRoot.setAdapter(
////                        new MessageRecylerViewAdapter(messageList)
//                        new MessageRecylerViewAdapter(mModel.getmRecentMessageList())
//                );
                //binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }




}