package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsListBinding;


public class ContactsRVAdapter extends RecyclerView.Adapter<ContactsRVAdapter.ViewHolder> {

    // creating variables for context and array list.

    private ArrayList<Contacts> contactsModalArrayList ;
    private FragmentContactsListBinding binding;

    // creating a constructor
    public ContactsRVAdapter(ArrayList<Contacts> contactsModalArrayList) {
        this.contactsModalArrayList = contactsModalArrayList;
    }

    @NonNull
    @Override
    public ContactsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ContactsRVAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contacts_card, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull ContactsRVAdapter.ViewHolder holder, int position) {
        // getting data from array list in our modal.
        Contacts modal = contactsModalArrayList.get(position);
        // on below line we are setting data to our text view.
        holder.contactTV.setText(modal.getFullName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(
                        ContactsFragmentDirections.actionNavigationContactsToContactsProfile(
                                modal.getFullName(), modal.getContactEmail()));
            }
        });
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        contactsModalArrayList.clear();
        if (charText.length() == 0) {
            contactsModalArrayList.addAll(contactsModalArrayList);
        } else {
            for (Contacts wp : contactsModalArrayList) {
                if (wp.getFullName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    contactsModalArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return contactsModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // on below line creating a variable
        // for our image view and text view.
        private ImageView contactIV;
        private TextView contactTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our image view and text view.
            contactIV = itemView.findViewById(R.id.image_contact);
            contactTV = itemView.findViewById(R.id.contact_name);
        }
    }
}