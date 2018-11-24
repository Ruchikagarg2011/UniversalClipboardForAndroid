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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pramod.firebase.R;
import java.util.ArrayList;
import java.util.Map;

public class DeviceCustomAdapter extends ArrayAdapter<Device> {
    Context context;
    int layoutResourceId;

    ArrayList<Device> data = new ArrayList<Device>();
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    private static final String key = "devices/" + FirebaseAuth.getInstance().getUid();


    public DeviceCustomAdapter(Context context, int layoutResourceId, ArrayList<Device> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(final int position, View view, ViewGroup parent){
        View row = view;
        DeviceDetails details = null;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            details = new DeviceDetails();
            details.textDeviceName = (TextView) row.findViewById(R.id.device_name);
            details.textIPAddress =(TextView) row.findViewById(R.id.ip_name);
            details.btnToggle =(ToggleButton)row.findViewById(R.id.toogle);
            details.btnDelete =(Button)row.findViewById(R.id.delete);
            row.setTag(details);
        } else {
            details =(DeviceDetails)row.getTag();
        }

        Device device = data.get(position);
        details.textDeviceName.setText(device.getDeviceName());
        details.textIPAddress.setText(device.getIpName());

        details.btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Device device = data.get(position);
                    device.state = "ON";
                } else {
                    // The toggle is disabled
                    Device device = data.get(position);
                    device.state = "OFF";
                }
                notifyDataSetChanged();
            }
        });

        details.btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Delete Button Clicked", "**********");
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

    static class DeviceDetails {
        TextView textDeviceName;
        TextView textIPAddress;
        ToggleButton btnToggle;
        Button btnDelete;
    }
}
