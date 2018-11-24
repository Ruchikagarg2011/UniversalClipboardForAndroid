package com.pramod.firebase.storage;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ClipHistoryStore {

    Map<String, ClipHistory> map = new HashMap<>();

    public Map<String, ClipHistory> getClipContents() {
        return map;
    }

    public void addClipHistory(ClipHistory clipHistory) {
        map.put(clipHistory.getTimestamp(), clipHistory);
    }

    public static ClipHistoryStore fromObject(Object obj) {


        if (obj == null) {
            return null;
        }
        ClipHistoryStore store = new ClipHistoryStore();
        Map<String, Map<String, String>> mapper = (Map<String, Map<String, String>>) obj;
        for (String key : mapper.keySet()) {
            store.addClipHistory(new ClipHistory(mapper.get(key)));
        }
        return store;
    }

}
