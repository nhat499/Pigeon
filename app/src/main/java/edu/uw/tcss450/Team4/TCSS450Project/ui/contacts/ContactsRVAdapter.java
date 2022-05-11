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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.Team4.TCSS450Project.MainActivity;
import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentContactsListBinding;
import edu.uw.tcss450.Team4.TCSS450Project.ui.signIn.SignInFragmentDirections;


public class ContactsRVAdapter extends RecyclerView.Adapter<ContactsRVAdapter.ViewHolder> {

    // creating variables for context and array list.
    private Context context;
    private ArrayList<ContactsViewModel> contactsModalArrayList ;
    private FragmentContactsListBinding binding;

    // creating a constructor
    public ContactsRVAdapter(Context context, ArrayList<ContactsViewModel> contactsModalArrayList) {
        this.context = context;
        this.contactsModalArrayList = contactsModalArrayList;
    }

    @NonNull
    @Override
    public ContactsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ContactsRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_contacts_card, parent, false));

    }

    // below method is use for filtering data in our array list
    public void filterList(ArrayList<ContactsViewModel> filterllist) {
        // on below line we are passing filtered
        // array list in our original array list
        contactsModalArrayList = filterllist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsRVAdapter.ViewHolder holder, int position) {
        // getting data from array list in our modal.
        ContactsViewModel modal = contactsModalArrayList.get(position);
        // on below line we are setting data to our text view.
        holder.contactTV.setText(modal.getUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are opening a new activity and passing data to it.
                Intent i = new Intent(context, ContactDetailActivity.class);
                i.putExtra("name", modal.getUserName());
                i.putExtra("contact", modal.getContactNumber());
                // on below line we are starting a new activity,
                context.startActivity(i);
            }
        });
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