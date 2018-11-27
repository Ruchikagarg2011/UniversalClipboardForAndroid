package com.ruchika.device;

import com.pramod.firebase.Constants;

import java.util.Map;

public class Device {
    String deviceName;
    String ipName;



    String state = Constants.STATE_ON;



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

    public void setState(String state) {
        this.state = state;
    }
    public String getDeviceName() {
        return deviceName;
    }

    public String getIpName() {
        return ipName;
    }

    public String getState() {
        return state;
    }
    public Device(String deviceName, String ipName) {
        super();
        this.deviceName = deviceName;
        this.ipName = ipName;
    }


}

