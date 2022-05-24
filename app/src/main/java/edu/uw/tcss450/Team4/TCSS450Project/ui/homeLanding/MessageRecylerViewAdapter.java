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
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentRecentMessageListBinding;

public class MessageRecylerViewAdapter extends
        RecyclerView.Adapter<MessageRecylerViewAdapter.MessageViewHolder> {

    //Store all of the Message to present
    private final List<HashMap<String,String>> mList;
    private FragmentRecentMessageListBinding binding;

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
        private String mMessage;

        public MessageViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentRecentMessageBinding.bind(view);

            // set button to (click show more of blog) (but i dont have that)
            //binding.buittonMore.setOnClickListener(this::handleMoreOrLess);
        }

        void setMessage(final HashMap<String,String> m) {
            //mMessage = message;
            //Log.d("recyle", "setMessage: " + message);
            //Log.d("TAG", "setMessage: " +m.get());
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
                            HomeLandingFragmentDirections.actionNavigationHomeToNavigationChat();
                    direction.setRoom((Integer.valueOf(m.get("chatId"))));
                    Navigation.findNavController(view).navigate(direction);
                }
            });

            //binding.RecentText.setText("test");
            // navi to full post ( Idont have)
//            binding.buttonFullPost.setOnClickListener(view -> {
//                Navigation.findNavController(mView).navigate(
//                        BlogListFragmentDirections
//                                .actionNavigationBlogsToBlogPostFragment(blog));
//            });

            // update post ?
//            binding.textTitle.setText(blog.getTitle());
//            binding.textPubdate.setText(blog.getPubDate());
//            //Use methods in the HTML class to format the HTML found in the text
//            final String preview = Html.fromHtml(
//                    blog.getTeaser(),
//                    Html.FROM_HTML_MODE_COMPACT)
//                    .toString().substring(0,100) //just a preview of the teaser
//                    + "...";
//            binding.textPreview.setText(preview);
//            displayPreview();
        }
    }
}
