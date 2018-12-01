package com.pramod.firebase.services;

import com.pramod.firebase.custom_notification;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.pramod.firebase.Constants;
import com.pramod.firebase.clipboard.ClipboardHandler;
import com.pramod.firebase.storage.ClipHistory;
import com.pramod.firebase.storage.DeviceStore;
import com.pramod.firebase.util.AndroidUtils;
import com.pramod.firebase.util.KeyStore;
import com.pramod.firebase.util.RDBHandler;


import java.security.Key;
import java.util.Calendar;
import java.util.Map;

public class ClipboardMonitorService extends Service {

    ClipboardChangeListener changeListener;
    private ClipboardManager clipboardManager;
    FirebaseDatabase database;


    @Override
    public void onCreate() {
        setupElements();
    }

    void setupElements() {
        changeListener = new ClipboardChangeListener();
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            clipboardManager.addPrimaryClipChangedListener(changeListener);
        }
        database = FirebaseDatabase.getInstance();
        monitorFirebaseClipboardChanges();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * When Clipboard is changed on a device , this method gets called.
     */
    class ClipboardChangeListener implements ClipboardManager.OnPrimaryClipChangedListener {

        /**
         * Things on this call.
         * 1. Store entry in KeyStore.getMainClipKey
         * 2. Udpate device history.
         * <p>
         * TODO: Scenario to test when Copy image done through screenshot
         */
        @Override
        public void onPrimaryClipChanged() {
            ClipData data = clipboardManager.getPrimaryClip();
            if (data != null) {
                String text = data.getItemAt(0).getText().toString();
                saveInFirebase(text, Constants.TYPE_TEXT);
            }
        }
    }

    public static void saveInFirebase(String text, String messageType) {
        ClipHistory history = new ClipHistory(
                KeyStore.getDeviceName(),
                text,
                messageType,
                AndroidUtils.getTimeStamp());

        if (!history.equals(lastValue)) {
            lastValue = history;
            RDBHandler.getInstance().write(KeyStore.getMainClipKeyForUser(), history);
            RDBHandler.getInstance().write(KeyStore.getClipboardKeyForCurrentTime(), history);
            Log.i(Constants.TAG, "Writing to Firebase");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        startBroadCaster();
    }

    void startBroadCaster() {
        Intent broadcastIntent = new Intent(this, BroadcastReceiverService.class);
        sendBroadcast(broadcastIntent);
    }

    private static ClipHistory lastValue;

    public DBEventListener listener = new DBEventListener();

    void monitorFirebaseClipboardChanges() {


        DatabaseReference dbReference = database.getReference(KeyStore.getMainClipKeyForUser());
        dbReference.addValueEventListener(listener);
    }

    class DBEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                ClipHistory val = new ClipHistory((Map<String, String>) dataSnapshot.getValue());

                //Same device copy and duplicate copy check.
                if (val.equals(lastValue) || val.getDeviceName().equals(KeyStore.getDeviceName())) {
                    //return;
                }
                if (val.isText()) {
                    ClipboardHandler.setInClipboard(val.getClipContent(), getApplicationContext());
                    lastValue = val;
                } else {
                    custom_notification.displayNotification(getApplicationContext(), val.getClipContent());
                    lastValue = val;
                }
                /*else if (val.getMessageType().equals(Constants.TYPE_IMAGE)) {

                                    //String token = task.getResult().getToken();
                    //sendNotification(val.getClipContent());

                        //should change
                    custom_notification.displayNotification(getApplicationContext(),val.getClipContent());
                    lastValue = val;
                }
                if (val.getMessageType().equals(Constants.TYPE_FILE)) {
                    ClipboardHandler.setInClipboard(val.getClipContent(), getApplicationContext());
                    lastValue = val;
                }*/

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    }

}

