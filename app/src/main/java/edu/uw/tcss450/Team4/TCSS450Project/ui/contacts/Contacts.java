package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import androidx.annotation.Nullable;

/**
 * View model for the Contacts Fragment
 *
 * @author team4
 * @version May 2022
 */
public class Contacts {
    // variables for our user name
    // and contact number.
    private int memberId;
    private String userFirstName;
    private String userLastName;
    private String contactEmail;

    // constructor
    public Contacts(String userFirstName, String userLastName, String contactEmail) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.contactEmail = contactEmail;
    }

    // on below line we have
    // created getter and setter
    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactNumber(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    public int getMessageId() {
        return memberId;
    }



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
