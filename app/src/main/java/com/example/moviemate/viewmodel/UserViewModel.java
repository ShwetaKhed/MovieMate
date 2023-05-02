package com.example.moviemate.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private MutableLiveData<String> loginEmail = new MutableLiveData<>();
    private MutableLiveData<String> dateOfBirth = new MutableLiveData<>();

    public void setLoginEmail(String email) {
        loginEmail.setValue(email);
    }

    public LiveData<String> getLoginEmail() {
        return loginEmail;
    }

    public void setDateOfBirth(String dob) {
        dateOfBirth.setValue(dob);
    }

    public LiveData<String> getDateOfBirth() {
        return dateOfBirth;
    }
}
