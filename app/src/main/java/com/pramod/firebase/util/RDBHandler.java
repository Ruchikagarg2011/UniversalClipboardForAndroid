package com.pramod.firebase.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RDBHandler {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static RDBHandler instance = new RDBHandler();

    public static RDBHandler getInstance() {
        return instance;
    }



    public void write(String key, Object value) {
        DatabaseReference dbReference = database.getReference(key);
        dbReference.setValue(value);

    }

}
