package com.pramod.firebase.storage;


import com.google.firebase.auth.FirebaseAuth;
import com.pramod.firebase.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Device {
   public String deviceName;
   public String ipName;
   public String deviceId;
   public String state = Constants.STATE_ON;



    public Device(Map<String, String> map) {
        deviceName = map.get("deviceName");
        ipName = map.get("ipName");
        state = map.get("state");
        this.deviceId = map.get("deviceId");
    }


    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setIpName(String ipName) {
        this.ipName = ipName;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getDeviceName() {
        return deviceName;
    }

    public String getIpName() {
        return ipName;
    }

    public String getState() {
        return state;
    }

    public String getDeviceId() {
        return deviceId;
    }


    public Device(String deviceId, String deviceName, String ipName) {
        super();
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.ipName = ipName;
    }

    public Device() {

    }


}