package com.pramod.firebase.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pramod.firebase.GlobalHomeActivity;
import com.pramod.firebase.storage.Device;
import com.pramod.firebase.util.KeyStore;

import java.util.Map;

public class DeviceMonitorService extends Service {
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    private static final String key = "devices/" + FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Override
    public void onCreate() {
        monitorDeviceChanges();
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
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    void monitorDeviceChanges() {
        DatabaseReference dbReference = fdb.getReference(key);
        dbReference.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Device device = new Device((Map<String, String>) dataSnapshot.getValue());
                String deviceName = dataSnapshot.getKey();
                String state = device.getState();
                if(device.getState().equals("1")  && device.getDeviceName().equals(KeyStore.getDeviceName())) {
                    if(!isMyServiceRunning(ClipboardMonitorService.class)){
                        startServices();
                    }
                }
                else if(device.getState().equals("0") && device.getDeviceName().equals(KeyStore.getDeviceName())) {
                    if(isMyServiceRunning(ClipboardMonitorService.class)){
                        stopService();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void stopService() {
        if (!isMyServiceRunning(ClipboardMonitorService.class)) {
            stopService(new Intent(this, ClipboardMonitorService.class));
        }
    }

    public void startServices() {
        if (!isMyServiceRunning(ClipboardMonitorService.class)) {
            startService(new Intent(this, ClipboardMonitorService.class));
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

}
