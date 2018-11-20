package com.pramod.firebase.storage;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Device {
    String deviceName;
    String ipName;
    String state;

    public Device(Map<String, String> map) {
        deviceName = map.get("deviceName");
        ipName = map.get("ipName");
        state = map.get("state");
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setIpName(String ipName) {
        this.ipName = ipName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getIpName() {
        return ipName;
    }

    public Device(String deviceName, String ipName) {
        super();
        this.deviceName = deviceName;
        this.ipName = ipName;
    }


}