package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import java.io.Serializable;

public class ChatRoom implements Serializable {
    private final String mTitle;
    private int mRoomNumber;

    public static class Builder {
        private final String mTitle;
        private int mRoomNumber;

        public Builder(String title, int room) {
            this.mTitle = title;
            this.mRoomNumber = room;
        }
        public ChatRoom build() {
            return new ChatRoom(this);
        }
    }

    private ChatRoom(final Builder builder) {
        this.mTitle = builder.mTitle;
        this.mRoomNumber = builder.mRoomNumber;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getRoomNumber() {
        return mRoomNumber;
    }

}
