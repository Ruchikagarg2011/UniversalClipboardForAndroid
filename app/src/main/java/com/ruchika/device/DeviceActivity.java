package com.ruchika.device;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.firebase.R;
import android.support.v4.app.Fragment;

import org.json.JSONObject;

import java.util.ArrayList;


public class DeviceActivity extends Fragment {

    ListView deviceList;
    DeviceCustomAdapter deviceCustomAdapter;
    ArrayList<Device> deviceArray = new ArrayList<Device>();
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();

    public DeviceActivity() {
        // Required empty public constructor
        setupElements();
    }

    private static final String key = "demoKey/";

    void setupElements() {
        DatabaseReference reference = fdb.getReference(key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                JSONObject obj = (JSONObject)dataSnapshot.getValue();
                //Device device = Device.fromJSON(obj);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        deviceArray.add(new Device("mobile", "1.1.1.1"));
        deviceArray.add(new Device("mobile2", "2.2.2.2"));


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.device_list, container, false);
        deviceCustomAdapter = new DeviceCustomAdapter(this.getActivity(), R.layout.device_details, deviceArray);
        deviceList =(ListView)view.findViewById(R.id.devicelist);
       // deviceList.setItemsCanFocus(false);
        deviceList.setAdapter(deviceCustomAdapter);
        return view;
    }

}
