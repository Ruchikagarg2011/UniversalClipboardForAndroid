package jai.clipboard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pramod.firebase.R;

import java.util.ArrayList;

public class ClipboardAdapter  extends ArrayAdapter<Clipboard> {

    Activity context;
    int layoutId;
    ArrayList<Clipboard> clipContents = new ArrayList<Clipboard>();

    public ClipboardAdapter(Activity context, int layoutId, ArrayList<Clipboard> clipContents) {
        super(context, layoutId,clipContents);
        this.context = context;
        this.layoutId = layoutId;
        this.clipContents = clipContents;
    }

    public View getView(int position, View view, ViewGroup parent) {


//        LayoutInflater inflator = context.getLayoutInflater();
//        View rowView = inflator.inflate(R.layout.clipboard_list, null, true);
//
//        TextView deviceTitleTxtView = (TextView) rowView.findViewById(R.id.device_title);
//        deviceTitleTxtView.setText(clipObj.getDeviceName());
//        TextView clipboardContentTxtView = (TextView) rowView.findViewById(R.id.clipboard_content);
//        clipboardContentTxtView.setText(clipboard.getCopyContent());
//        return rowView;


        View row = view;
        ClipboardDetails clipboard = null;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutId, parent, false);
            clipboard = new ClipboardDetails();
            clipboard.deviceTitleTxtView = (TextView) row.findViewById(R.id.device_title);
            clipboard.clipboardContentTxtView =(TextView) row.findViewById(R.id.clipboard_content);

            row.setTag(clipboard);
        } else {
            clipboard =(ClipboardDetails) row.getTag();
        }

        Clipboard clipDetails = clipContents.get(position);
        clipboard.deviceTitleTxtView.setText(clipDetails.getDeviceName());
        clipboard.clipboardContentTxtView.setText(clipDetails.getCopyContent());
        return row;

    }

    static class ClipboardDetails {
        TextView deviceTitleTxtView;
        TextView clipboardContentTxtView;

    }
}
