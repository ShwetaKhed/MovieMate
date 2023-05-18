package com.example.moviemate;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public  class DatabaseController {
    private DatabaseReference mDatabase;
    private void GetDataBaseInstance() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

}
