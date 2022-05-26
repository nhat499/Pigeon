package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatRoomCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.ui.contacts.Contacts;

public class AddFromContactsRecyclerViewAdapter extends RecyclerView.Adapter<AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder> {

    private final List<Contacts> mContacts;

    public AddFromContactsRecyclerViewAdapter(@NonNull List<Contacts> contacts) {
        this.mContacts = contacts;
    }

    @NonNull
    @Override
    public AddFromContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddFromContactsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_add_from_contacts_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder holder, int position) {
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
        public FragmentChatRoomCardBinding binding;
        private Contacts mContact;

        public AddFromContactsViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatRoomCardBinding.bind(view);
            binding.textTitle.setText("ASdf");

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
        void setContact(final Contacts contact) {
            mContact = contact;
            binding.textTitle.setText("AEIOU");
        }
    }
}
