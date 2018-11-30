package com.pramod.firebase.services;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.pramod.firebase.Constants;

import com.pramod.firebase.GlobalHomeActivity;
import com.shweta.shareFile.FirebaseFileHandler;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();

        if (Constants.DOWNLOAD_ACTION.equals(action)) {
            String uri = intent.getStringExtra("url");
            Log.d("downloading ",uri.toString());
            FirebaseFileHandler.downloadFile(uri);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(intent.getExtras().getInt("id"));

        }
        if (Constants.CANCEL_ACTION.equals(action)) {
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(intent.getExtras().getInt("id"));

        }

    }




}