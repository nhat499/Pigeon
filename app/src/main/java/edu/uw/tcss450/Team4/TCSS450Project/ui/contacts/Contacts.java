package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import androidx.annotation.Nullable;

/**
 * View model for the Contacts Fragment
 *
 * @author team4
 * @version May 2022
 */
public class Contacts {

    private int memberId;

    private String userFirstName;

    private String userLastName;

    private String contactEmail;

    public Contacts(String userFirstName, String userLastName, String contactEmail, int memberId) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.contactEmail = contactEmail;
        this.memberId = memberId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getFullName() { return getUserFirstName() + " " + getUserLastName(); }

    /**
     * Provides equality solely based on MessageId.
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof Contacts) {
            result = memberId == ((Contacts) other).memberId;
        }
        return result;
    }

}
