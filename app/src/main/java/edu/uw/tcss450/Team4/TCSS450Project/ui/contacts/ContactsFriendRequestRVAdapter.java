package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsListBinding;


public class ContactsFriendRequestRVAdapter extends RecyclerView.Adapter<ContactsFriendRequestRVAdapter.ViewHolder> {

    // creating variables for context and array list.

    //private ArrayList<Contacts> contactsModalArrayList ;
    private List<Contacts> contactsList = null;
    private FragmentContactsListBinding binding;

    // creating a constructor
    public ContactsFriendRequestRVAdapter(List<Contacts> contactsList) {
        this.contactsList = contactsList;
    }

    @NonNull
    @Override
    public ContactsFriendRequestRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_friendrequest_card, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull ContactsFriendRequestRVAdapter.ViewHolder holder, int position) {
        // getting data from array list in our modal.
        Contacts modal = contactsList.get(position);
//        // on below line we are setting data to our text view.
        holder.nameContact.setText(modal.getFullName());
//        holder.deleteContact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(
//                        ContactsFragmentDirections.actionNavigationContactsToContactsProfile(
//                                modal.getFullName(), modal.getContactEmail()));
//            }
//        });


    }
    // Filter Class
    public void setFilteredList(List<Contacts> filteredList) {
        this.contactsList = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // on below line creating a variable
        // for our image view and text view.
        private TextView nameContact;
        private ImageView rejectContact;
        private ImageView acceptContact;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our image view and text view.
            acceptContact = itemView.findViewById(R.id.accept_contact);
            nameContact = itemView.findViewById(R.id.contact_name);
            rejectContact = itemView.findViewById(R.id.reject_contact);
        }
    }

}