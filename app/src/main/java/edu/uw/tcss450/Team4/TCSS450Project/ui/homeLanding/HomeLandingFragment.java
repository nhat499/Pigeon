package edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.json.JSONObject;

import java.util.HashMap;


import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentHomeLandingBinding;

/**
 * Class to define the fragment lifecycle for the HomeLanding Fragment
 *
 * @author team4
 * @version May 2022
 */
public class HomeLandingFragment extends Fragment {
    private FragmentHomeLandingBinding mBinding;
    private HomeLandingViewModel mHomeLandModel;
    private RecentMessageListViewModel mMessages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeLandingFragmentArgs  args = HomeLandingFragmentArgs
                .fromBundle(getActivity().getIntent().getExtras());
        mHomeLandModel = new ViewModelProvider(getActivity())
                .get(HomeLandingViewModel.class);
        mHomeLandModel.connect(args.getJwt(), args.getEmail()); // should be swap once fix

        mMessages = new ViewModelProvider(getActivity()).get(RecentMessageListViewModel.class);
        mMessages.connectGet(args.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        mBinding = FragmentHomeLandingBinding.inflate(inflater);

        // Inflate the layout for this fragment
        return mBinding.getRoot();
//        return inflater.inflate(R.layout.fragment_home_landing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Observer<HashMap<String,String>> userInfoObserver = map -> {
            mBinding.numOfContact.setText(map.get("numOfContact"));
            mBinding.numOfMessage.setText(map.get("numOfMessages"));
            mBinding.profileName.setText(map.get("name"));
            mBinding.profileEmail.setText(map.get("email"));
        };
        mHomeLandModel.HomeLandingObserver(getViewLifecycleOwner(), userInfoObserver);

        mMessages.addRecentMessageObserver(getViewLifecycleOwner(), m -> {
            mBinding.listRoot.setAdapter(
//                        new MessageRecylerViewAdapter(messageList)
                    new MessageRecylerViewAdapter(mMessages.getmRecentMessageList())
            );
        });
    }
}