package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

/**
 * View model for the Contacts Fragment
 *
 * @author team4
 * @version May 2022
 */
public class ContactsViewModel {
    // variables for our user name
    // and contact number.
    private String userName;
    private String contactNumber;

    // constructor
    public ContactsViewModel(String userName, String contactNumber) {
        this.userName = userName;
        this.contactNumber = contactNumber;
    }

    // on below line we have
    // created getter and setter
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

}
