package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

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

import java.util.ArrayList;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;

/**
 * Class to define the fragment lifecycle for the Contacts Fragment
 *
 * @author team4
 * @version May 2022
 */
public class ContactsFragment extends Fragment {
    private FragmentContactsListBinding binding;
    //    private String contactName, contactNumber;
//    private TextView contactTV, nameTV;
//    private ImageView contactIV, callIV, messageIV;
//    private ArrayList<ContactsViewModel> contactsModalArrayList = new ArrayList<>(100);
//    private RecyclerView recyclerView;
//    private ContactsRVAdapter contactRVAdapter;
    private UserInfoViewModel mUserModel;
    private ContactsViewModel mContactsViewModel;
    private static final int HARD_CODED_CHAT_ID = 0;

    public ContactsFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts_list, container, false);
////        contactsModalArrayList.get(0).setUserName("Shirwa Ahmed");
////        contactsModalArrayList.get(0).setContactNumber("2064122111");
//        contactRVAdapter = new ContactsRVAdapter(getActivity(), contactsModalArrayList);
//        recyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView_Contact);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(contactRVAdapter);
//        getContacts();
        return v;
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
        //On button click, navigate to registration
        FragmentContactsListBinding binding = FragmentContactsListBinding.bind(getView());

        final RecyclerView rv = binding.listRoot;

        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new ContactsRVAdapter(
                getActivity(), (ArrayList<Contacts>) mContactsViewModel.getContactsListByMemberId(133)));
        binding.buttonToCreateContact.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ContactsFragmentDirections.actionNavigationContactsToCreateNewContactActivity()
                ));

        mContactsViewModel.addContactObserver(0, getViewLifecycleOwner(),
                list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    //binding.swipeContainer.setRefreshing(false);
                });


    }



//    @SuppressLint("Range")
//    private void getContacts() {
//        Context applicationContext = MainActivity.getContextOfApplication();
//        // this method is use to read contact from users device.
//        // on below line we are creating a string variables for
//        // our contact id and display name.
//        String contactId = "";
//        String displayName = "";
//        // on below line we are calling our content resolver for getting contacts
//        Cursor cursor = applicationContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
//        // on blow line we are checking the count for our cursor.
//        if (cursor.getCount() > 0) {
//            // if the count is greater than 0 then we are running a loop to move our cursor to next.
//            while (cursor.moveToNext()) {
//                // on below line we are getting the phone number.
//                @SuppressLint("Range") int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
//                if (hasPhoneNumber > 0) {
//                    // we are checking if the has phone number is > 0
//                    // on below line we are getting our contact id and user name for that contact
//                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                    // on below line we are calling a content resolver and making a query
//                    Cursor phoneCursor = applicationContext.getContentResolver().query(
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                            new String[]{contactId},
//                            null);
//                    // on below line we are moving our cursor to next position.
//                    if (phoneCursor.moveToNext()) {
//                        // on below line we are getting the phone number for our users and then adding the name along with phone number in array list.
//                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        contactsModalArrayList.add(new ContactsViewModel(displayName, phoneNumber));
//                    }
//                    // on below line we are closing our phone cursor.
//                    phoneCursor.close();
//                }
//            }
//        }
//        // on below line we are closing our cursor.
//        cursor.close();
//        // on below line we are hiding our progress bar and notifying our adapter class.
//        contactRVAdapter.notifyDataSetChanged();
//    }



}
