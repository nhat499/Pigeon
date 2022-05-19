package edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeLandingFragmentArgs  args = HomeLandingFragmentArgs
                .fromBundle(getActivity().getIntent().getExtras());
        mHomeLandModel = new ViewModelProvider(getActivity())
                .get(HomeLandingViewModel.class);
        mHomeLandModel.connect(args.getJwt(), args.getEmail()); // should be swap once fix

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
//        UserInfoViewModel model = new ViewModelProvider(getActivity())
//                .get(UserInfoViewModel.class);
//        Log.e("test", "onViewCreated: " + mHomeLandModel.userInfo.get("numOfContact") );
        //mBinding.numOfContact.setText(mHomeLandModel.mResponse.getValue().get("numOfContact"));
        //String a = mHomeLandModel.mResponse.getValue().toString();
        //Log.d("test", "onViewCreated: " + a);
        ///while(mHomeLandModel.mResponse.getValue().isEmpty()) {}
        FragmentHomeLandingBinding binding = FragmentHomeLandingBinding.bind(getView());
//
//        mHomeLandModel.HomeLandingObserver(getViewLifecycleOwner(), s -> {
//            binding.layoutRoot.set
//        });

        Observer<HashMap<String,String>> userInfoObserver = map -> {
            mBinding.numOfContact.setText(map.get("numOfContact"));
            mBinding.numOfMessage.setText(map.get("numOfMessages"));
            mBinding.profileName.setText(map.get("name"));
            mBinding.profileEmail.setText(map.get("email"));
        };
        mHomeLandModel.HomeLandingObserver(getViewLifecycleOwner(), userInfoObserver);

        //System.out.println("testetst" + mHomeLandModel.getMResponse().toString());
//        Log.e("test respond", "onCreateView: " +
//                mHomeLandModel.mResponse.getValue().g);
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {

        } else {

        }
    }
}