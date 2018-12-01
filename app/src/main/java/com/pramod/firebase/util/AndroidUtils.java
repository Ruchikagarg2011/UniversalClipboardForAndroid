package com.pramod.firebase.util;

import android.content.Context;

import java.sql.Timestamp;

public class AndroidUtils {

    void navigatePage(Context ctx, Class toClass) {

    }

    public static String getTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }


}
