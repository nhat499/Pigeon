package edu.uw.tcss450.Team4.TCSS450Project.ui.registration;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentEmailVerificationBinding;

/**
 * Class to define the fragment lifecycle for the EmailVerification Fragment
 *
 * @author team4
 * @version May 2022
 */
public class EmailVerificationFragment extends Fragment {

    private FragmentEmailVerificationBinding binding;

    private Bundle args;

    public EmailVerificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        args = getArguments();
        binding = FragmentEmailVerificationBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void navigateToVerification(final String email, final String password) {
        EmailVerificationFragmentDirections.ActionEmailVerificationFragmentToSignInFragment
                directions = EmailVerificationFragmentDirections.actionEmailVerificationFragmentToSignInFragment();
        directions.setEmail(email);
        directions.setPassword(password);
        Navigation.findNavController(getView()).navigate(directions);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.ButtonCheckVerification.setOnClickListener(button ->
                navigateToVerification(
                        args.getString("email"),
                        args.getString("password")
                ));
    }
}