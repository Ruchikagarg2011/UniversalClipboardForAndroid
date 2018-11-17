package com.pramod.firebase.services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;

import com.pramod.firebase.util.RDBHandler;

public class ClipboardMonitorService extends Service {

    ClipboardChangeListener changeListener;
    private ClipboardManager clipboardManager;

    @Override
    public void onCreate() {
        setupElements();
    }

    void setupElements() {
        changeListener = new ClipboardChangeListener();
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(changeListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ClipboardChangeListener implements ClipboardManager.OnPrimaryClipChangedListener {


        String DEMO_KEY = "demoKey";

        @Override
        public void onPrimaryClipChanged() {
            ClipData data = clipboardManager.getPrimaryClip();
            String text = data.getItemAt(0).getText().toString();
            RDBHandler.getInstance().write(DEMO_KEY, text);
        }
    }


}
