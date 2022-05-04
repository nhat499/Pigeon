package edu.uw.tcss450.Team4.TCSS450Project.ui.registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentEmailVerificationBinding;
import edu.uw.tcss450.Team4.TCSS450Project.ui.signIn.SignInFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
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

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Log.d("EmailVe", "EmailVe: " + args);
//        Log.d("EmailVe", "EmailVe: " + args.getString("jwt"));
//        Log.d("EmailVe", "EmailVe: " + args.getString("email"));
        EmailVerificationFragmentDirections.ActionEmailVerificationFragmentToMainActivity
                directions = EmailVerificationFragmentDirections.
                actionEmailVerificationFragmentToMainActivity(args.getString("jwt"), args.getString("email"));
        binding.ButtonCheckVerification.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(directions));
    }
}