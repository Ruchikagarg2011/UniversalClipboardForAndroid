package com.pramod.firebase.storage;

import java.util.Map;

public class ClipHistory {

    String clipData;

    ClipHistory(Map<String, String> map) {
        this.clipData = map.get("clipData");
    }

}
