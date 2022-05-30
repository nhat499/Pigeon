package edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentRecentMessageBinding;


public class MessageRecylerViewAdapter extends
        RecyclerView.Adapter<MessageRecylerViewAdapter.MessageViewHolder> {

    //Store all of the Message to present
    private final List<HashMap<String,String>> mList;

    public MessageRecylerViewAdapter(List<HashMap<String, String>> items) {
        this.mList = items;
    }

    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Edit onCreateViewHolder() to inflate the layout fragment_blog_card:
        return new MessageViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_recent_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecylerViewAdapter.MessageViewHolder holder, int position) {
        holder.setMessage(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Message Recycler View.
     */
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentRecentMessageBinding binding;

        public MessageViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentRecentMessageBinding.bind(view);
        }

        void setMessage(final HashMap<String,String> m) {
            binding.RecentChatName.setText((m.get("chatName")));
            String message = m.get("name") +": " + m.get("message");
            if (message.length() > 95) {
                message = message.substring(0,92) + "...";
            }
            binding.RecentText.setText(message);
            binding.time.setText("date: " + m.get("time"));

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeLandingFragmentDirections.ActionNavigationHomeToNavigationChat direction =
                            HomeLandingFragmentDirections.actionNavigationHomeToNavigationChat(m.get("chatName"));
                    direction.setRoom((Integer.valueOf(m.get("chatId"))));
                    Navigation.findNavController(view).navigate(direction);
                }
            });
        }
    }
}
