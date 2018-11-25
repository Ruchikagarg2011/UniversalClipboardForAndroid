package com.pramod.firebase.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import com.pramod.firebase.Constants;

public class ClipboardHandler {

    public static void setInClipboard(String text, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(Constants.CLIPBOARD_LABEL, text));
        Log.i(Constants.TAG, "Stored in clipboard: " + text);
    }

}
