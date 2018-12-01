package com.pramod.firebase;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shweta.shareFile.FirebaseFileHandler;

public class Transparent extends Activity {

    public static final String CHANNEL_ID = "Notification";
    public static final String CHANNEL_NAME = "Notification";
    public static final String CHANNEL_DESC = "Notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        setUpIntent();
        setUpNotificationChannel();

    }
    void setUpIntent(){
        Intent intent = getIntent();
        FirebaseFileHandler.sendIntentHandler(intent);
    }

    void setUpNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            custom_notification.createNotificationChannel(manager);
        }
    }
}
