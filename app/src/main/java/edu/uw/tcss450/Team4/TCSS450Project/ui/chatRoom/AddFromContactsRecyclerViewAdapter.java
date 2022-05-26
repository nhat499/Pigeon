package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddFromContactsBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentAddFromContactsCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatRoomCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.Contacts;

public class AddFromContactsRecyclerViewAdapter extends RecyclerView.Adapter<AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder> {

    private final Map<Contacts, Boolean> mExpandedFlags;

    private final List<Contacts> mContacts;

    public AddFromContactsRecyclerViewAdapter(@NonNull List<Contacts> contacts) {
        this.mContacts = contacts;
        Log.e("ASDFADSDSFDAS", this.mContacts.toString());
        mExpandedFlags = mContacts.stream()
                .collect(Collectors.toMap(Function.identity(), blog -> false));
    }

    @NonNull
    @Override
    public AddFromContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddFromContactsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_add_from_contacts_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddFromContactsViewHolder holder, int position) {
        Log.e("ASDFADSDgfadgfadgagSFDAS", "gfdasgfdasgfdagadsg");
        holder.setContact(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class AddFromContactsViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentAddFromContactsCardBinding binding;
        private Contacts mContact;

        public AddFromContactsViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentAddFromContactsCardBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ChatRoomListFragmentDirections.ActionNavigationChatRoomListToNavigationChat directions =
//                            ChatRoomListFragmentDirections.actionNavigationChatRoomListToNavigationChat();
//                    directions.setRoom(mChatRoom.getRoomNumber());
//                    Navigation.findNavController(view)
//                            .navigate(directions);
                }
            });
        }

        private void handleMoreOrLess(final View button) {
            mExpandedFlags.put(mContact, !mExpandedFlags.get(mContact));
//            displayPreview();
        }

        void setContact(final Contacts contact) {
            mContact = contact;
            binding.textName.setText(contact.getFullName());
        }
    }
}
