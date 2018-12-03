package com.pramod.firebase.services;


import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.pramod.firebase.Constants;
import android.app.Activity;

import com.pramod.firebase.GlobalHomeActivity;
import com.pramod.firebase.Transparent;
import com.shweta.shareFile.FirebaseFileHandler;


public class NotificationReceiver extends BroadcastReceiver {

    private static int STORAGE_PERMISSION_CODE = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Constants.DOWNLOAD_ACTION.equals(action) && intent.getStringExtra("type").equals("2")) {
            String uri = intent.getStringExtra("url");
            Log.d("downloading ", uri.toString());
            FirebaseFileHandler.getINSTANCE().downloadFile(context, uri);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(intent.getExtras().getInt("id"));

        }
        if (Constants.DOWNLOAD_ACTION.equals(action) && intent.getStringExtra("type").equals("5")) {
            String uri = intent.getStringExtra("url");
            Log.d("downloading ", uri.toString());
            FirebaseFileHandler.getINSTANCE().downloadPdfFile(context, uri);
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