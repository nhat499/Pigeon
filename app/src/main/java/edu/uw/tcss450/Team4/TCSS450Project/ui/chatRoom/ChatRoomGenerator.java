package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import java.util.Arrays;
import java.util.List;

public class ChatRoomGenerator {

    private static final ChatRoom[] Chats;
    private static final String[] RoomTitles = {"Test Chat 1", "Test Chat 2", "Global Chat", "Empty Chat"};
    public static final int COUNT = 4;

    static {
        Chats = new ChatRoom[COUNT];
        for (int i = 0; i < Chats.length; i++) {
            Chats[i] = new ChatRoom
                    .Builder(RoomTitles[i], i)
                    .build();
        }
    }

    public static List<ChatRoom> getBlogList() {
        return Arrays.asList(Chats);
    }

    public static ChatRoom[] getChats() {
        return Arrays.copyOf(Chats, Chats.length);
    }

    private ChatRoomGenerator() { }
}
