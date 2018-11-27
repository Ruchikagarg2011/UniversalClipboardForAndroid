package com.ruchika.device;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pramod.firebase.Constants;
import com.pramod.firebase.R;
import com.pramod.firebase.util.KeyStore;

import java.util.ArrayList;
import java.util.Map;

public class DeviceCustomAdapter extends ArrayAdapter<Device> {
    Context context;
    int layoutResourceId;

    ArrayList<Device> data = new ArrayList<Device>();
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
  //  private static final String key = "devices/" + FirebaseAuth.getInstance().getUid();
    private static final String key = KeyStore.getDevicesKeyForUser();
    TextView textDeviceName;
    TextView textIPAddress;
    Switch btnSwitch;
    ImageButton btnDelete;


    public DeviceCustomAdapter(Context context, int layoutResourceId, ArrayList<Device> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(final int position, View view, ViewGroup parent){
        View row = view;
        Device device = data.get(position);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            textDeviceName = (TextView) row.findViewById(R.id.device_name);
            textIPAddress =(TextView) row.findViewById(R.id.ip_name);
            btnSwitch =(Switch) row.findViewById(R.id.btnSwitch);
            btnDelete =(ImageButton) row.findViewById(R.id.delete);
         //   row.setTag(details);
          //  details =(DeviceDetails)row.getTag();


        textDeviceName.setText(device.getDeviceName());
        textIPAddress.setText(device.getIpName());
        //Log.d("currentState",device.getState());

        if (device.getState().equals("0")) {
            btnSwitch.setChecked(false);
        }

     //   details.btnSwitch.setText(device.getState());

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Device device = data.get(position);
                    device.state = Constants.STATE_ON;
                    Log.d("stateON",device.state);
                    String mapKey = device.deviceName;
                    DatabaseReference dbReference = fdb.getReference(key).child(mapKey).child("state");
                    Log.d("dbreference1",dbReference.toString());
                    dbReference.setValue("1");

                } else {
                    // The toggle is disabled
                    Device device = data.get(position);
                    device.state = Constants.STATE_OFF;
                    Log.d("stateOFF",device.state);
                    String mapKey = device.deviceName;
                    DatabaseReference dbReference = fdb.getReference(key).child(mapKey).child("state");
                    Log.d("dbreference2",dbReference.toString());
                    dbReference.setValue("0");

                }
                notifyDataSetChanged();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Delete button Clicked", Toast.LENGTH_LONG).show();
                Device device = data.get(position);
                DeviceStore storeObject = new DeviceStore();
                Map<String,Device> map = storeObject.getDevices();
                String mapKey = device.deviceName;
                map.remove(mapKey);
                data.remove(position);

                DatabaseReference dbReference = fdb.getReference(key).child(mapKey);
                dbReference.removeValue();

                notifyDataSetChanged();
            }
        });

        return row;
    }


}
