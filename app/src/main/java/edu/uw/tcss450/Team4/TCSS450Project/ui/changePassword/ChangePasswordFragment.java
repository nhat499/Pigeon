package edu.uw.tcss450.Team4.TCSS450Project.ui.changePassword;

import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkPwdUpperCase;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.auth0.android.jwt.JWT;
import org.json.JSONException;
import org.json.JSONObject;
import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChangePasswordBinding;
import edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding mBinding;

    private ChangePasswordViewModel mChangeModel;

    private final PasswordValidator mPassWordValidatorLength = checkPwdLength(7);

    private final PasswordValidator mPassWordValidatorUppercase = checkPwdUpperCase();

    private final PasswordValidator mPassWordValidatorLowercase = checkPwdLowerCase();

    private final PasswordValidator mPassWordValidatorNumber = checkPwdDigit();

    private final PasswordValidator mPassWordValidatorSymbol = checkPwdSpecialChar();

    private final PasswordValidator mPassWordValidatorSpace = checkExcludeWhiteSpace();

    private final PasswordValidator mPassWordValidator = checkPwdLength(7)
            .and(checkPwdSpecialChar())
            .and(checkExcludeWhiteSpace())
            .and(checkPwdDigit())
            .and(checkPwdLowerCase().and(checkPwdUpperCase()));

    private final PasswordValidator mPassWordValidatorMatch = checkClientPredicate(pwd ->
            pwd.equals(mBinding.editPassword3.getText().toString().trim()));

    private String lengthMark, uppercaseMark, lowercaseMark, numberMark, symbolMark, spaceMark = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangeModel = new ViewModelProvider(getActivity())
                .get(ChangePasswordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChangePasswordBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.buttonRequest.setOnClickListener(this::attemptRequest);
        mChangeModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptRequest(final View button) { validatePasswordRequirements(); }

    private void validatePasswordRequirements() {
        // Are used to set the individual check marks or x marks.
        mPassWordValidatorLength.processResult(
                mPassWordValidatorLength.apply(mBinding.editPassword2.getText().toString()),
                this::setLengthMarkCheck,
                result -> setXMark(1));
        mPassWordValidatorUppercase.processResult(
                mPassWordValidatorUppercase.apply(mBinding.editPassword2.getText().toString()),
                this::setUppercaseMarkCheck,
                result -> setXMark(2));
        mPassWordValidatorLowercase.processResult(
                mPassWordValidatorLowercase.apply(mBinding.editPassword2.getText().toString()),
                this::setLowercaseMarkCheck,
                result -> setXMark(3));
        mPassWordValidatorNumber.processResult(
                mPassWordValidatorNumber.apply(mBinding.editPassword2.getText().toString()),
                this::setNumberMarkCheck,
                result -> setXMark(4));
        mPassWordValidatorSymbol.processResult(
                mPassWordValidatorSymbol.apply(mBinding.editPassword2.getText().toString()),
                this::setSymbolMarkCheck,
                result -> setXMark(5));
        mPassWordValidatorSpace.processResult(
                mPassWordValidatorSpace.apply(mBinding.editPassword2.getText().toString()),
                this::setSpaceMarkCheck,
                result -> setXMark(6));
        mPassWordValidator.processResult(
                mPassWordValidator.apply(mBinding.editPassword2.getText().toString()),
                this::validatePasswordsMatch,
                result -> mBinding.editPassword2.setError("Your password must have: \n" +
                        lengthMark + " More Than 7 Characters \n" +
                        uppercaseMark + " Uppercase Letters \n" +
                        lowercaseMark + " Lowercase Letters \n" +
                        numberMark + " At least 1 number \n" +
                        symbolMark + " At least 1 symbol: @#$%&*!? \n" +
                        spaceMark + " No spaces"));
    }

    private void validatePasswordsMatch() {
        mPassWordValidatorMatch.processResult(
                mPassWordValidatorMatch.apply(mBinding.editPassword2.getText().toString().trim()),
                this::verifyAuthWithServer,
                result -> mBinding.editPassword2.setError("Passwords must match."));
    }

    private void verifyAuthWithServer() {
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
        JWT jwt = new JWT(token);
        String email = jwt.getClaim("email").asString();
        mChangeModel.connect(
                email,
                mBinding.editPassword1.getText().toString(),
                mBinding.editPassword2.getText().toString()
        );
    }

    // Having a method for processResult is required, so this is here as placeholder.
    private void setLengthMarkCheck() {
        lengthMark = "✓";
    }
    private void setUppercaseMarkCheck() {
        uppercaseMark = "✓";
    }
    private void setLowercaseMarkCheck() {
        lowercaseMark = "✓";
    }
    private void setNumberMarkCheck() {
        numberMark = "✓";
    }
    private void setSymbolMarkCheck() {
        symbolMark = "✓";
    }
    private void setSpaceMarkCheck() { spaceMark = "✓"; }

    private void setXMark(int choose) {
        if (choose == 1) {
            lengthMark = "✕";
        } else if (choose == 2) {
            uppercaseMark = "✕";
        } else if (choose == 3) {
            lowercaseMark = "✕";
        } else if (choose == 4) {
            numberMark = "✕";
        } else if (choose == 5) {
            symbolMark = "✕";
        } else if (choose == 6) {
            spaceMark = "✕";
        }
    }

    private void navigateToMainActivity() {
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.authenticationActivity);
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
                    mBinding.editPassword1.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToMainActivity();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}