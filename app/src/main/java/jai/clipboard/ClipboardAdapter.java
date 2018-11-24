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
import com.pramod.firebase.storage.ClipHistory;

import java.util.ArrayList;

public class ClipboardAdapter  extends ArrayAdapter<ClipHistory> {

    Activity context;
    int layoutId;
    ArrayList<ClipHistory> clipContents = new ArrayList<ClipHistory>();

    public ClipboardAdapter(Activity context, int layoutId, ArrayList<ClipHistory> clipContents) {
        super(context, layoutId,clipContents);
        this.context = context;
        this.layoutId = layoutId;
        this.clipContents = clipContents;
    }

    public View getView(int position, View view, ViewGroup parent) {

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

        ClipHistory clipDetails = clipContents.get(position);
        clipboard.deviceTitleTxtView.setText(clipDetails.getDeviceName());
        clipboard.clipboardContentTxtView.setText(clipDetails.getClipContent());
        return row;

    }

    static class ClipboardDetails {
        TextView deviceTitleTxtView;
        TextView clipboardContentTxtView;

    }
}
