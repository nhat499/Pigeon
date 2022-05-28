//package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;
//
//import android.graphics.Color;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import edu.uw.tcss450.Team4.TCSS450Project.R;
//import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddFromContactsBinding;
//import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddFromContactsCardBinding;
//import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatRoomCardBinding;
//import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsCardBinding;
//import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.Contacts;
//
//public class AddFromContactsRecyclerViewAdapter extends RecyclerView.Adapter<AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder> {
//
//    private final Map<Contacts, Boolean> mExpandedFlags;
//
//    private final List<Contacts> mContacts;
//
//    private final List<Contacts> mAddedContacts;
//
//    public AddFromContactsRecyclerViewAdapter(@NonNull List<Contacts> contacts) {
//        this.mContacts = contacts;
//        this.mAddedContacts = new ArrayList<>();
//        mExpandedFlags = mContacts.stream()
//                .collect(Collectors.toMap(Function.identity(), blog -> false));
//    }
//
//    @NonNull
//    @Override
//    public AddFromContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new AddFromContactsViewHolder(LayoutInflater
//                .from(parent.getContext())
//                .inflate(R.layout.fragment_add_from_contacts_card, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AddFromContactsViewHolder holder, int position) {
//        holder.setContact(mContacts.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mContacts.size();
//    }
//
//    public List<Contacts> getAddedContacts() {
//        return mAddedContacts;
//    }
//
//    /**
//     * Objects from this class represent an Individual row View from the List
//     * of rows in the Blog Recycler View.
//     */
//    public class AddFromContactsViewHolder extends RecyclerView.ViewHolder {
//        public final View mView;
//        public FragmentAddFromContactsCardBinding binding;
//        private Contacts mSingleContact;
//
//        public AddFromContactsViewHolder(View view) {
//            super(view);
//            mView = view;
//            binding = FragmentAddFromContactsCardBinding.bind(view);
//            AddFromContactsFragment.test();
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    ChatRoomListFragmentDirections.ActionNavigationChatRoomListToNavigationChat directions =
////                            ChatRoomListFragmentDirections.actionNavigationChatRoomListToNavigationChat();
////                    directions.setRoom(mChatRoom.getRoomNumber());
////                    Navigation.findNavController(view)
////                            .navigate(directions);
//                }
//            });
//            binding.checkBox.setOnClickListener(check ->
//            {
//                if (binding.checkBox.isChecked()) {
//                    mAddedContacts.add(mSingleContact);
//                    Log.e("Checkbox Added : ", "" + mAddedContacts.toString());
//                } else {
//                    mAddedContacts.remove(mSingleContact);
//                    Log.e("Checkbox Remove : ", "" + mAddedContacts.toString());
//                }
//            });
//        }
//
//        private void handleMoreOrLess(final View button) {
//            mExpandedFlags.put(mSingleContact, !mExpandedFlags.get(mSingleContact));
////            displayPreview();
//        }
//
//        void setContact(final Contacts contact) {
//            mSingleContact = contact;
//            binding.textName.setText(contact.getFullName());
//        }
//    }
//}
