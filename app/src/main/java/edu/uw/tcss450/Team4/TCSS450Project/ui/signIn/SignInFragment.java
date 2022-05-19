package edu.uw.tcss450.Team4.TCSS450Project.ui.signIn;

import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.auth0.android.jwt.JWT;
import org.json.JSONException;
import org.json.JSONObject;
import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentSignInBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.PushyTokenViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.AddMemberViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator;

/**
 * Class to define the fragment lifecycle for the SignIn Fragment
 *
 * @author team4
 * @version May 2022
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding mBinding;

    private SignInViewModel mSignInModel;

    private PushyTokenViewModel mPushyTokenViewModel;

    private AddMemberViewModel mAddMemberViewModel;

    private UserInfoViewModel mUserViewModel;

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator = checkPwdLength(1)
            .and(checkExcludeWhiteSpace());

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_jwt))) {
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            JWT jwt = new JWT(token);
            if (!jwt.isExpired(0)) {
                String email = jwt.getClaim("email").asString();
                navigateToSuccess(email, token);
                return;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
        mAddMemberViewModel = new ViewModelProvider(getActivity()).get(AddMemberViewModel.class);
        // disable back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("BACK_BUTTON", "Back button clicked");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentSignInBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //On button click, navigate to registration
        mBinding.buttonToRegister.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionSignInFragmentToRegistrationFragment()
                ));

        //On button click, navigate to forgot password fragment
        mBinding.textForgotPassword.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment()
                ));

        //On button click navigate to main activity
        mBinding.buttonSignin.setOnClickListener(this::attemptSignIn);
        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        //Set email and password after coming from registration or forgot password
        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        mBinding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        mBinding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());

        //Don't allow sign in until pushy token retrieved
        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                mBinding.buttonSignin.setEnabled(!token.isEmpty()));
        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);
    }

    private void attemptSignIn(final View button) {
        validateEmail();
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(mBinding.editEmail.getText().toString().trim()),
                this::validatePassword,
                result -> mBinding.editEmail.setError("Please enter a valid Email address."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(mBinding.editPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> mBinding.editPassword.setError("Please enter a valid Password."));
    }

    private void verifyAuthWithServer() {
        mSignInModel.connect(
                mBinding.editEmail.getText().toString(),
                mBinding.editPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the result of connect()
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     * @param email users email
     * @param jwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt) {
        // Adds users to the global and test chat rooms upon sign in.
        // Can change this to be upon registration instead.
        mAddMemberViewModel.addMember(jwt, email, 0);
        mAddMemberViewModel.addMember(jwt, email, 1);
        mAddMemberViewModel.addMember(jwt, email, 2);
        //Save jwt if remember me is checked for auto sign in
        if (mBinding.checkBoxRememberMe.isChecked()) {
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
        }

        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionSignInFragmentToMainActivity(email, jwt));
        getActivity().finish();
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
                    mBinding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    if ((int) response.get("verification") == 1) {
                        mUserViewModel = new ViewModelProvider(getActivity(),
                                new UserInfoViewModel.UserInfoViewModelFactory(
                                        mBinding.editEmail.getText().toString(),
                                        response.getString("token")
                                )).get(UserInfoViewModel.class);
                        sendPushyToken();
                        navigateToSuccess(
                                mBinding.editEmail.getText().toString(),
                                response.getString("token")
                        );
                    } else {
                        mBinding.editEmail.setError("Must verify email before signing in.\nCheck your email for verification instructions.");
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                mBinding.editEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                navigateToSuccess(
                        mBinding.editEmail.getText().toString(),
                        mUserViewModel.getmJwt()
                );
            }
        }
    }

    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getmJwt());
    }
}