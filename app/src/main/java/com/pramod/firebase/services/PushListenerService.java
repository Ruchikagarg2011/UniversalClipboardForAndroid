package com.pramod.firebase.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pramod.firebase.Constants;
import com.pramod.firebase.clipboard.ClipboardHandler;


public class PushListenerService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        Log.d(Constants.TAG, "FCMToken:" + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(Constants.TAG, remoteMessage.getFrom());
        String message = remoteMessage.getData().toString();
        runOnMainThread(message);
    }

    Handler handler = new Handler(Looper.getMainLooper());

    void runOnMainThread(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ClipboardHandler.setInClipboard(message, getApplicationContext());
                Log.d(Constants.TAG, "Stored in clipboard");
            }
        });
        Log.d(Constants.TAG, "SEnding execution");
    }
}
