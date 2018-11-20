package com.ruchika.device;

public class Device {
    String deviceName;
    String ipName;
    String state;

    public void setDeviceName(String deviceName){
        this.deviceName =deviceName;
    }

    public void setIpName(String ipName){
        this.ipName =ipName;
    }

    public String getDeviceName(){
        return deviceName;
    }

    public String getIpName() {
        return ipName;
    }

    public Device(String deviceName,String ipName){
        super();
        this.deviceName = deviceName;
        this.ipName = ipName;
    }


}

