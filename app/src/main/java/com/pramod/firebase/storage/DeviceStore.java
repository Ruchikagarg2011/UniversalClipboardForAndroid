package com.pramod.firebase.storage;

import com.pramod.firebase.util.KeyStore;
import com.pramod.firebase.util.RDBHandler;

import java.util.HashMap;
import java.util.Map;

public class DeviceStore {

    private static DeviceStore deviceStore = new DeviceStore();

    public static DeviceStore getInstance() {
        return deviceStore;
    }

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

    public void storeCurrentDevice() {
        Device device = new Device(KeyStore.getDeviceName(), KeyStore.getLocalIpAddress());
        addDevice(device);
        RDBHandler.getInstance().write(KeyStore.getDevicesKeyForCurrentDevice(KeyStore.getDeviceName()), device);
    }

}
