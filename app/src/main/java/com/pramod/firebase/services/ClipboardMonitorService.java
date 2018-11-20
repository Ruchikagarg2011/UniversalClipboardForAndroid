package com.pramod.firebase.services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.android.device.DeviceName;
import com.pramod.firebase.clipboard.ClipboardHandler;
import com.pramod.firebase.storage.Device;
import com.pramod.firebase.storage.DeviceStore;
import com.pramod.firebase.util.RDBHandler;

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
        clipboardManager.addPrimaryClipChangedListener(changeListener);
        database = FirebaseDatabase.getInstance();
        realtimeSync();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    String DEMO_KEY = "demoKey2/Itcf3GlE0WW4odnO5YK2JatOZHf2";

    class ClipboardChangeListener implements ClipboardManager.OnPrimaryClipChangedListener {

        @Override
        public void onPrimaryClipChanged() {
            String deviceName = DeviceName.getDeviceName();
            String secondDevice = Build.MODEL + "_ " + Build.BRAND + "_" + Build.ID;
            ClipData data = clipboardManager.getPrimaryClip();
            String text = data.getItemAt(0).getText().toString();
//            Map<String, Object> obj = new HashMap();
//            obj.put("supername", "supervalue");
//            obj.put("dupername", "dupervalue");
//            obj.put("devicename", new Device("polodevice", "dolodevice"));
//            obj.put("mydevicename", deviceName);
//            obj.put("secondDevice", secondDevice);
//            //obj.put("devicename", new )

            DeviceStore store = new DeviceStore();
            Device device = new Device("ruchikadevice", "1.2.3.4");
            store.addDevice(device);
            RDBHandler.getInstance().write(DEMO_KEY, store.getDevices());
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


    void realtimeSync() {
        DatabaseReference dbReference = database.getReference(DEMO_KEY);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DeviceStore val = DeviceStore.fromObject(dataSnapshot.getValue());
                String value = String.valueOf(dataSnapshot.getValue());
                ClipboardHandler.setInClipboard(value, getApplicationContext());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
