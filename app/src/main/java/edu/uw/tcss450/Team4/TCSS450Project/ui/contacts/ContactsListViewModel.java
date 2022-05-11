package edu.uw.tcss450.Team4.TCSS450Project.ui.contacts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

public class ContactsListViewModel extends AndroidViewModel {
    private MutableLiveData<List<ContactsFragment>> mContactList;

    public ContactsListViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>();
        mContactList.setValue(new ArrayList<>());
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ContactsFragment>> observer) {
        mContactList.observe(owner, observer);
    }
}
