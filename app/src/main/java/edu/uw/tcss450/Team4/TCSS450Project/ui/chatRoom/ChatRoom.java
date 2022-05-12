package edu.uw.tcss450.Team4.TCSS450Project.ui.chatRoom;

import java.io.Serializable;

public class ChatRoom implements Serializable {
    private final String mTitle;

    public static class Builder {
        private final String mTitle;

        public Builder(String title) {
            this.mTitle = title;
        }
        public ChatRoom build() {
            return new ChatRoom(this);
        }
    }

    private ChatRoom(final Builder builder) {
        this.mTitle = builder.mTitle;
    }

    public String getTitle() {
        return mTitle;
    }
}
