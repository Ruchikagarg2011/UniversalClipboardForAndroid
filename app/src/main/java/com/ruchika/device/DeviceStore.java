package com.ruchika.device;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeviceStore {

    Map<String, Device> map = new HashMap<>();

    public Map<String, Device> getDevices() {
        return map;
    }

    public void addDevice(Device device) {
        map.put(device.getDeviceName(), device);
    }

    public static DeviceStore fromObject(Object obj) {
        if (obj == null) {
            return null;
        }
        DeviceStore store = new DeviceStore();
        Map<String, Map<String, String>> mapper = (Map<String, Map<String, String>>) obj;
        for (String key : mapper.keySet()) {
            store.addDevice(new Device(mapper.get(key)));
        }
        return store;
    }

    public static ArrayList<Device> getDeviceArray(DeviceStore obj){
        ArrayList<Device> deviceArray = new ArrayList<Device>();
        Map<String, Device> map = obj.getDevices();
        for (String key : map.keySet()) {
            Device deviceObj = map .get(key);
            String deviceName = deviceObj.getDeviceName();
            String ipName = deviceObj.getIpName();
            deviceArray.add(new Device(deviceName, ipName));
        }
        return deviceArray;
    }

}
