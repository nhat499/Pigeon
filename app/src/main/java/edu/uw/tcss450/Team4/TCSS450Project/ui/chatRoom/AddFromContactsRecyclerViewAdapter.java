package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatRoomCardBinding;

public class AddFromContactsRecyclerViewAdapter extends RecyclerView.Adapter<AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder> {

    public AddFromContactsRecyclerViewAdapter() {
        // Needs implementation.
    }

    @NonNull
    @Override
    public AddFromContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_add_from_contacts_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddFromContactsRecyclerViewAdapter.AddFromContactsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class AddFromContactsViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatRoomCardBinding binding;

        public AddFromContactsViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatRoomCardBinding.bind(view);

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
//            binding.bu.setOnClickListener(this::handleMoreOrLess);

        }

        private void handleMoreOrLess(final View button) {
//            mExpandedFlags.put(mChatRoom, !mExpandedFlags.get(mChatRoom));
//            displayPreview();
        }

//        /**
//         * Helper used to determine if the preview should be displayed or not.
//         */
//        private void displayPreview() {
//            if (mExpandedFlags.get(mBlog)) {
//                binding.textPreview.setVisibility(View.VISIBLE);
//                binding.buittonMore.setImageIcon(
//                        Icon.createWithResource(
//                                mView.getContext(),
//                                R.drawable.ic_less_grey_24dp));
//            } else {
//                binding.textPreview.setVisibility(View.GONE);
//                binding.buittonMore.setImageIcon(
//                        Icon.createWithResource(
//                                mView.getContext(),
//                                R.drawable.ic_more_grey_24dp));
//            }
//        }

        void setChatRoom(final ChatRoom chat) {
//            mChatRoom = chat;
//            binding.textTitle.setText(chat.getTitle());

            // This handles setting the number of notifications of each chat room by looping
            // through the current notification list and if the given chatID matches with any
            // number in the notification list, we +1 to the notification number to the room.
//            if (!(mNotificationList == null) && !mNotificationList.isEmpty()) {
//                int count = 0;
//                for (int i = 0; i < mNotificationList.size(); i++) {
//                    if (mNotificationList.get(i) == mChatRoom.getRoomNumber()) {
//                        count++;
//                    }
//                }
//                if (count != 0) {
//                    binding.textNotification.setText(count + "");
//                    binding.textNotification.setTextColor(Color.RED);
//                }
//            }
//            binding.buttonFullPost.setOnClickListener(view -> {
//                Navigation.findNavController(mView).navigate(
//                        ChatRoomListFragmentDirections.actionNavigationBlogsToBlogPostFragment(blog));
//            });
//            binding.textTitle.setText(blog.getTitle());
//            binding.textPubdate.setText(blog.getPubDate());
            //Use methods in the HTML class to format the HTML found in the text
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
