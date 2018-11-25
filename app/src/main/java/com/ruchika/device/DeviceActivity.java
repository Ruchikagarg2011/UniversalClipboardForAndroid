package com.ruchika.device;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.firebase.R;
import com.pramod.firebase.util.RDBHandler;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;


public class DeviceActivity extends Fragment {

    ListView deviceList;
    DeviceCustomAdapter deviceCustomAdapter;
    ArrayList<Device> deviceArray = new ArrayList<Device>();
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    private static final String key = "devices/" +FirebaseAuth.getInstance().getCurrentUser().getUid();
    String value;
    String deviceName,ipName;

    public DeviceActivity() {
        // Required empty public constructor
    //    setupElements();
      //  getElements();
    }


    void setupElements(){

        DeviceStore store = new DeviceStore();
        Device device = new Device("testdevice3", "1.2.3.4");
        Device device2 = new Device("testdevice4", "7.8.3.4");

        store.addDevice(device);
        store.addDevice(device2);
        RDBHandler.getInstance().write(key, store.getDevices());
    }


/*    void getElements(){
        DatabaseReference dbReference = fdb.getReference(key);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DeviceStore val = DeviceStore.fromObject(dataSnapshot.getValue());
           //     deviceArray = DeviceStore.getDeviceArray(val);

                Map<String, Device> map = val.getDevices();
                for (String key : map.keySet()) {
                    Device deviceObj = map.get(key);
                    deviceName = deviceObj.getDeviceName();
                    ipName = deviceObj.getIpName();
                    deviceArray.add(new Device(deviceName, ipName));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    } */

    public void childActivity(){
        DatabaseReference dbReference = fdb.getReference(key);
        dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void  onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)  {
                Log.d("added",dataSnapshot.getValue().toString());
                Device device = new Device((Map<String, String>) dataSnapshot.getValue());
                Log.d("Ruchika",device.getDeviceName());

                deviceArray.add(device);
                deviceCustomAdapter.notifyDataSetChanged();

                //     deviceArray = DeviceStore.getDeviceArray(val);
                try {

                    /*DeviceStore val = DeviceStore.fromObject(dataSnapshot.getValue());

                    Map<String, Device> map = val.getDevices();
                    for (String key : map.keySet()) {
                        Device deviceObj = map.get(key);
                        deviceName = deviceObj.getDeviceName();
                        ipName = deviceObj.getIpName();
                        deviceArray.add(new Device(deviceName, ipName));
                    }
*/
                }catch (Exception ex) {
                    ex.printStackTrace();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                deviceArray = DeviceStore.getDeviceArray(val);
                Log.d("changed",dataSnapshot.getValue().toString());

                Device device = new Device((Map<String, String>) dataSnapshot.getValue());
                String deviceName = dataSnapshot.getKey();

                for(Device d : deviceArray){
                    if(d.getDeviceName().equals(deviceName)){
                         int index = deviceArray.indexOf(d);
                         Log.d("removed", index+"");
                         deviceArray.remove(index);
                         break;
                    }
                }
                deviceArray.add(device);


/*
                DeviceStore val = DeviceStore.fromObject(dataSnapshot.getValue());
                Map<String, Device> map = val.getDevices();
                for (String key : map.keySet()) {
                    Device deviceObj = map.get(key);
                    deviceName = deviceObj.getDeviceName();
                    ipName = deviceObj.getIpName();
                    deviceArray.add(new Device(deviceName, ipName));
                }
*/

                deviceCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                Log.d("deleted",dataSnapshot.getValue().toString());

                Device device = new Device((Map<String, String>) dataSnapshot.getValue());
                String deviceName = dataSnapshot.getKey();

                for(Device d : deviceArray){
                    if(d.getDeviceName().equals(deviceName)){
                        int index = deviceArray.indexOf(d);
                        Log.d("removed", index+"");
                        deviceArray.remove(index);
                        break;
                    }
                }
                deviceCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        childActivity();

        View view= inflater.inflate(R.layout.device_list, container, false);
        deviceCustomAdapter = new DeviceCustomAdapter(this.getActivity(), R.layout.device_details, deviceArray);
        deviceList =(ListView)view.findViewById(R.id.devicelist);
       // deviceList.setItemsCanFocus(false);
        deviceList.setAdapter(deviceCustomAdapter);
        return view;
    }

}

