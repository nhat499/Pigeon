package edu.uw.tcss450.Team4.TCSS450Project.ui.chat;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import edu.uw.tcss450.Team4.TCSS450Project.ui.homeLanding.HomeLandingViewModel;

/**
 * Encapsulate chat message details.
 */
public final class ChatMessage implements Serializable {

    private final int mMessageId;
    private final String mMessage;
    private String mSender;
    private final String mTimeStamp;
    private final String mFirstName;
    private final String mLastName;


    public ChatMessage(int messageId, String message, String sender, String timeStamp, String firstname, String lastname) {
        mMessageId = messageId;
        mMessage = message;
        mSender = sender;
        mTimeStamp = timeStamp;
        mFirstName = firstname;
        mLastName = lastname;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static ChatMessage createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        return new ChatMessage(msg.getInt("messageid"),
                msg.getString("message"),
                msg.getString("email"),
                msg.getString("timestamp"),
                msg.getString("firstname"),
                msg.getString("lastname"));
    }

    public String getMessage() {
        return mMessage;
    }

    public String getSender() {
        return mSender;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public int getMessageId() {
        return mMessageId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    /**
     * Provides equality solely based on MessageId.
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ChatMessage) {
            result = mMessageId == ((ChatMessage) other).mMessageId;
        }
        return result;
    }
}
