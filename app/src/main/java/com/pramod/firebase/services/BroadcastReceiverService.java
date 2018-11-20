package com.pramod.firebase.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastReceiverService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ClipboardMonitorService.class));
        Log.i(this.getClass().getName(), "ReStarted ClipboardMonitorService!");
    }

}
