package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.text.Html;
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
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatRoomCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatFragment;
import edu.uw.tcss450.Team4.TCSS450Project.ui.registration.RegistrationFragmentDirections;
import edu.uw.tcss450.Team4.TCSS450Project.ui.signIn.SignInFragmentDirections;

public class ChatRoomRecyclerViewAdapter extends RecyclerView.Adapter<ChatRoomRecyclerViewAdapter.ChatRoomViewHolder>  {

    //Store the expanded state for each List item, true -> expanded, false -> not
    private final Map<ChatRoom, Boolean> mExpandedFlags;

    //Chat rooms to present.
    private final List<ChatRoom> mChatRooms;

    private final List<Integer> mNotificationList;

    public ChatRoomRecyclerViewAdapter(@NonNull List<ChatRoom> items, List<Integer> notificationList) {
        this.mChatRooms = items;
        this.mNotificationList = notificationList;
        mExpandedFlags = mChatRooms.stream()
                .collect(Collectors.toMap(Function.identity(), blog -> false));
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatRoomViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_room_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        holder.setChatRoom(mChatRooms.get(position));
    }

    @Override
    public int getItemCount() {
        return mChatRooms.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatRoomCardBinding binding;
        private ChatRoom mChatRoom;


        public ChatRoomViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatRoomCardBinding.bind(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChatRoomListFragmentDirections.ActionNavigationChatRoomListToNavigationChat directions =
                            ChatRoomListFragmentDirections.actionNavigationChatRoomListToNavigationChat();
                    directions.setRoom(mChatRoom.getRoomNumber());
                    Navigation.findNavController(view)
                            .navigate(directions);
                }
            });
//            binding.bu.setOnClickListener(this::handleMoreOrLess);

        }

        private void handleMoreOrLess(final View button) {
            mExpandedFlags.put(mChatRoom, !mExpandedFlags.get(mChatRoom));
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
            mChatRoom = chat;
            binding.textTitle.setText(chat.getTitle());

            // This handles setting the number of notifications of each chat room by looping
            // through the current notification list and if the given chatID matches with any
            // number in the notification list, we +1 to the notification number to the room.
            if (!(mNotificationList == null) && !mNotificationList.isEmpty()) {
                int count = 0;
                for (int i = 0; i < mNotificationList.size(); i++) {
                    if (mNotificationList.get(i) == mChatRoom.getRoomNumber()) {
                        count++;
                    }
                }
                if (count != 0) {
                    binding.textNotification.setText(count + "");
                    binding.textNotification.setTextColor(Color.RED);
                }
            }
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
