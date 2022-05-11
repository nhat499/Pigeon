package edu.uw.tcss450.Team4.TCSS450Project.ui.signIn;

import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.*;
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
import org.json.JSONException;
import org.json.JSONObject;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentSignInBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.PushyTokenViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator;

/**
 * Class to define the fragment lifecycle for the SignIn Fragment
 *
 * @author team4
 * @version May 2022
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;

    private SignInViewModel mSignInModel;

    private PushyTokenViewModel mPushyTokenViewModel;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
        // disable back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Log.d("BACK_BUTTON", "Back button clicked");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //On button click, navigate to registration
        binding.buttonToRegister.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionSignInFragmentToRegistrationFragment()
                ));
        // set listener for resend verification
        //binding.textResendLink.setOnClickListener(this::attemptResendLink);
        // set listener for forgot password (either forgot password fragment or send email to email currently in editEmail)
        //binding.textForgotPassword.setOnClickListener(this::attemptForgotPassword);
        // set listener for remember me
        //if checked save login info in shared preferences maybe

        // On button click navigate to main activity
        binding.buttonSignin.setOnClickListener(this::attemptSignIn);

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());
//        don't allow sign in until pushy token retrieved
        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                binding.buttonSignin.setEnabled(!token.isEmpty()));
        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);
    }

    private void attemptSignIn(final View button) {
        validateEmail();
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword.setError("Please enter a valid Password."));
    }

    private void verifyAuthWithServer() {
        mSignInModel.connect(
                binding.editEmail.getText().toString(),
                binding.editPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the result of connect()
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     * @param email users email
     * @param jwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt) {
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionSignInFragmentToMainActivity(email, jwt));
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
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    if ((int) response.get("verification") == 1) {
                        // save login info here if remember me is checked
                        mUserViewModel = new ViewModelProvider(getActivity(),
                                new UserInfoViewModel.UserInfoViewModelFactory(
                                        binding.editEmail.getText().toString(),
                                        response.getString("token")
                                )).get(UserInfoViewModel.class);
                        sendPushyToken();
                        navigateToSuccess(
                                binding.editEmail.getText().toString(),
                                response.getString("token")
                        );
                    } else {
                        binding.editEmail.setError("Must verify email before signing in.\nCheck your email for verification instructions.");
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
                binding.editEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                navigateToSuccess(
                        binding.editEmail.getText().toString(),
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