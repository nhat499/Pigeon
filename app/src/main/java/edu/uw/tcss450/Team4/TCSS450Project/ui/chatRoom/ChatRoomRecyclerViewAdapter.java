package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.Team4.TCSS450Project.R;
import edu.uw.tcss450.Team4.TCSS450Project.databinding.FragmentChatRoomCardBinding;
import edu.uw.tcss450.Team4.TCSS450Project.model.UserInfoViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatFragment;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatMessage;
import edu.uw.tcss450.Team4.TCSS450Project.ui.chat.ChatViewModel;
import edu.uw.tcss450.Team4.TCSS450Project.ui.registration.RegistrationFragmentDirections;
import edu.uw.tcss450.Team4.TCSS450Project.ui.signIn.SignInFragmentDirections;

public class ChatRoomRecyclerViewAdapter extends RecyclerView.Adapter<ChatRoomRecyclerViewAdapter.ChatRoomViewHolder>  {

    //Store the expanded state for each List item, true -> expanded, false -> not
    private final Map<ChatRoom, Boolean> mExpandedFlags;

    //Chat rooms to present.
    private final List<ChatRoom> mChatRooms;

    private final List<Integer> mNotificationList;

    private ChatViewModel mChatModel;

    private UserInfoViewModel mUserModel;

    public ChatRoomRecyclerViewAdapter(@NonNull UserInfoViewModel UserModel, ChatViewModel chatmodel, List<ChatRoom> items, List<Integer> notificationList) {
        this.mChatModel = chatmodel;
        this.mChatRooms = items;
        this.mUserModel = UserModel;
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
                            ChatRoomListFragmentDirections.actionNavigationChatRoomListToNavigationChat(mChatRoom.getTitle());
                    directions.setRoom(mChatRoom.getRoomNumber());
                    Navigation.findNavController(view)
                            .navigate(directions);
                }
            });
        }

        void setChatRoom(final ChatRoom chat) {
            mChatRoom = chat;
            binding.textTitle.setText(chat.getTitle());
            mChatModel.getFirstMessages(chat.getRoomNumber(), mUserModel.getmJwt());
            Log.e("a??", chat.getRoomNumber() + "");
            List<ChatMessage> temp = mChatModel.getMessageListByChatId(chat.getRoomNumber());
            if (!temp.isEmpty()) {
                binding.textRecent.setText(temp.get(temp.size() - 1).getFirstName() + ": " + temp.get(temp.size() - 1).getMessage());
            }
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
        }
    }
}
