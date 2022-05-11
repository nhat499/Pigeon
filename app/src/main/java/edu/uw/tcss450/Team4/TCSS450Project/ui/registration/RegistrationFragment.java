package edu.uw.tcss450.Team4.TCSS450Project.ui.registration;

import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator.checkPwdUpperCase;

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

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentRegistrationBinding;
import edu.uw.tcss450.Team4.TCSS450Project.utils.PasswordValidator;

/**
 * Class to define the fragment lifecycle for the Registration Fragment
 *
 * @author team4
 * @version May 2022
 */
public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;

    private RegistrationViewModel mRegisterModel;

    private PasswordValidator mNameValidator = checkPwdLength(1);

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().and(checkPwdUpperCase()));

    // Used for setting the marks as a checkmark or x for each password requirement.
    private PasswordValidator mPassWordValidatorLength =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkExcludeWhiteSpace());

    private PasswordValidator mPassWordValidatorUppercase =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdUpperCase())
                    .and(checkExcludeWhiteSpace());

    private PasswordValidator mPassWordValidatorLowercase =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    private PasswordValidator mPassWordValidatorNumber =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit());

    private PasswordValidator mPassWordValidatorSymbol =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace());


    private String lengthMark = "";

    private String uppercaseMark = "";

    private String lowercaseMark = "";

    private String numberMark = "";

    private String symbolMark = "";

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegistrationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptRegister(final View button) {
        validateFirst();
    }

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name."));
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editLast.setError("Please enter a last name."));
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    private void validatePassword() {
        // Are used to set the individual check marks or x marks.
        mPassWordValidatorLength.processResult(
                mPassWordValidatorLength.apply(binding.editPassword1.getText().toString()),
                this::setLengthMarkCheck,
                result -> setXMark(1));
        mPassWordValidatorUppercase.processResult(
                mPassWordValidatorUppercase.apply(binding.editPassword1.getText().toString()),
                this::setUppercaseMarkCheck,
                result -> setXMark(2));
        mPassWordValidatorLowercase.processResult(
                mPassWordValidatorLowercase.apply(binding.editPassword1.getText().toString()),
                this::setLowercaseMarkCheck,
                result -> setXMark(3));
        mPassWordValidatorNumber.processResult(
                mPassWordValidatorNumber.apply(binding.editPassword1.getText().toString()),
                this::setNumberMarkCheck,
                result -> setXMark(4));
        mPassWordValidatorSymbol.processResult(
                mPassWordValidatorSymbol.apply(binding.editPassword1.getText().toString()),
                this::setSymbolMarkCheck,
                result -> setXMark(5));


        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Your password must have: \n" +
                        lengthMark + " More Than 7 Characters \n" +
                        uppercaseMark + " Uppercase Letters \n" +
                        lowercaseMark + " Lowercase Letters \n" +
                        numberMark + " Numbers \n" +
                        symbolMark + " Symbols"));
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
        }
    }

    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editPassword1.getText().toString());
//        This is an Asynchronous call. No statements after should rely on the
//        result of connect()

    }

    private void navigateToLogin(final String email, final String password) {
        RegistrationFragmentDirections.ActionRegistrationFragmentToEmailVerificationFragment directions =
                RegistrationFragmentDirections.actionRegistrationFragmentToEmailVerificationFragment(email, password);

        Navigation.findNavController(getView()).navigate(directions);
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
                navigateToLogin(
                        binding.editEmail.getText().toString(),
                        binding.editPassword1.getText().toString()
                );
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
