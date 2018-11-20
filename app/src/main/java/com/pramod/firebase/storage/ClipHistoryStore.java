package com.pramod.firebase.storage;

import java.util.HashMap;
import java.util.Map;

public class ClipHistoryStore {

    Map<String, ClipHistory> map = new HashMap<>();

    Map<String, ClipHistory> getDevices() {
        return map;
    }


}
