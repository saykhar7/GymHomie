package com.gymhomie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NameViewModel extends ViewModel {
    private final MutableLiveData<String> fullName = new MutableLiveData<>();

    public void setFullName(String firstName, String lastName) {
        fullName.setValue("Welcome Back\n" + firstName + " " + lastName);
    }

    public LiveData<String> getFullName() {
        return fullName;
    }
}
