package com.pramod.firebase.util;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.jaredrummler.android.device.DeviceName;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Enumeration;

public class KeyStore {

    public static String getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static String getDevicesKeyForUser() {
        return "devices/" + getCurrentUser();
    }

    public static String getDevicesKeyForCurrentDevice(ContentResolver cv) {
        String device = getDeviceId(cv);
        return "devices/" + getCurrentUser() + "/" + device;
    }


    public static String getClipboardKeyForUser() {
        return "clipboard/" + getCurrentUser();
    }

    public static String getClipboardHistoryKeyForUser() {
        return getClipboardKeyForUser() + "/history/";
    }

    public static String getMainClipKeyForUser() {
        return getClipboardKeyForUser() + "/mainclipdata/";
    }

   /* public static String getClipboardKeyForCurrentTime() {
        return getClipboardHistoryKeyForUser() + "/" + Calendar.getInstance().getTime().toString();
    }*/

    public static String getClipboardKeyForCurrentTime() {
        return getClipboardHistoryKeyForUser()  + Calendar.getInstance().getTime().toString();
    }

    public static String getDeviceName() {
        return DeviceName.getDeviceName();
    }

    public static String getDeviceId(ContentResolver cv) {
        return Settings.Secure.getString(cv, Settings.Secure.ANDROID_ID);
    }


    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }


}
