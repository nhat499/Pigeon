package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.content.ClipData;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsProfileBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;

/**
 * Class to define the fragment lifecycle for the Contacts Fragment
 *
 * @author team4
 * @version May 2022
 */
public class ContactsFragment extends Fragment{

    private FragmentContactsListBinding mBinding;
    private FragmentContactsCardBinding mBinding2;
    private ContactsRVAdapter contactsAdapter;
    private Bundle mArgs;
    private UserInfoViewModel mUserModel;
    private Button profileDelete;
    private ContactsViewModel mContactsViewModel;
    private SearchView editsearch;

    public ContactsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentContactsListBinding.inflate(inflater);
        mBinding2 = FragmentContactsCardBinding.inflate(inflater);
        mArgs = getArguments();
        return mBinding.getRoot();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mContactsViewModel = provider.get(ContactsViewModel.class);
        mContactsViewModel.getFirstContacts(mUserModel.getmJwt());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView rv = mBinding.listRoot;
        mBinding2.getRoot();
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        contactsAdapter = new ContactsRVAdapter((ArrayList<Contacts>) mContactsViewModel.getContactListValue());
        rv.setAdapter(contactsAdapter);

        //Search Contacts with SearchView
        editsearch = mBinding.search;
        editsearch.clearFocus();
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });


        mBinding.buttonToCreateContact.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ContactsFragmentDirections.actionNavigationContactsToCreateContact()
                ));

        mContactsViewModel.addContactObserver(getViewLifecycleOwner(),
                list -> {
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                });


        profileDelete = mBinding2.deleteContact;
        // on below line we are adding on click listener for our button.
        profileDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test respond", "onClick: " +mArgs.getString("email")  );
                mContactsViewModel.deleteContact(mUserModel.getmJwt(),mArgs.getString("email"));
            }
        });
        mContactsViewModel.addContactObserver(getViewLifecycleOwner(),
                list -> {
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                });
    }

    private void filterList(String text) {
        List<Contacts> filteredList = new ArrayList<>();
        for (Contacts item : (ArrayList<Contacts>) mContactsViewModel.getContactListValue()){
            if(item.getFullName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(getActivity(), "no data found", Toast.LENGTH_SHORT).show();
        }else{
            contactsAdapter.setFilteredList(filteredList);
        }
    }

}
