package com.example.moviemate.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.ClipData.Item;
public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    public SharedViewModel(){
        mText = new MutableLiveData<String>();
    }

    public void setMessage(String message) {
        mText.setValue(message);
    }
    public LiveData<String> getText() {
        return mText;
    }

    private  MutableLiveData<Item> mapviewItem = null;
    public void setMapViewItem(Item item)
    {
        mapviewItem.setValue(item);
    }
    public LiveData<Item> getMapViewItem() {
        return mapviewItem;
    }

    private MutableLiveData<String> startDate = new MutableLiveData<>();

    public void setStartDate(String dob) {
        startDate.setValue(dob);
    }

    public LiveData<String> getStartDate() {
        return startDate;
    }

    private MutableLiveData<String> endDate = new MutableLiveData<>();

    public void setEndDate(String dob) {
        endDate.setValue(dob);
    }

    public LiveData<String> getEndDate() {
        return endDate;
    }
}

