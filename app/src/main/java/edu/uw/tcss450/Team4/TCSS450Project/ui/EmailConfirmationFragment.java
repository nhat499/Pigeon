package edu.uw.tcss450.Team4.TCSS450Project.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentEmailConfirmationBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailConfirmationFragment extends Fragment {

    private FragmentEmailConfirmationBinding mBinding;

    private Bundle mArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEmailConfirmationBinding.inflate(inflater);
        mArgs = getArguments();
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    private void navigateToSignIn(final String email, final String password) {
        EmailConfirmationFragmentDirections.ActionEmailConfirmationFragmentToSignInFragment
                directions = EmailConfirmationFragmentDirections.actionEmailConfirmationFragmentToSignInFragment();
        directions.setEmail(email);
        directions.setPassword(password);
        Navigation.findNavController(getView()).navigate(directions);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBinding.buttonReturn.setOnClickListener(button ->
                navigateToSignIn(
                        mArgs.getString("email"),
                        mArgs.getString("password")
                ));
        String description = mArgs.getString("type").equals("registration") ?
                "verify your email." : "update your password.";
        mBinding.textDescription.setText("We sent an email to "
            + mArgs.getString("email")
            + " with a link to "
            + description
            + " Please check your email and follow the instructions.");
    }

}