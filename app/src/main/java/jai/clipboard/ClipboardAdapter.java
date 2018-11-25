package jai.clipboard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pramod.firebase.R;
import com.pramod.firebase.storage.ClipHistory;
import com.pramod.firebase.storage.ClipHistoryStore;

import java.util.ArrayList;
import java.util.Map;

public class ClipboardAdapter  extends ArrayAdapter<ClipHistory> {

    Activity context;
    int layoutId;
    ArrayList<ClipHistory> clipContents = new ArrayList<ClipHistory>();
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    private static String key = "clipboard/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/history";

    public ClipboardAdapter(Activity context, int layoutId, ArrayList<ClipHistory> clipContents) {
        super(context, layoutId,clipContents);
        this.context = context;
        this.layoutId = layoutId;
        this.clipContents = clipContents;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        View row = view;
        ClipboardDetails clipboard = null;


        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutId, parent, false);
            clipboard = new ClipboardDetails();
            clipboard.deviceTitleTxtView = (TextView) row.findViewById(R.id.device_title);
            clipboard.clipboardContentTxtView =(TextView) row.findViewById(R.id.clipboard_content);
            clipboard.delBtn = (ImageButton) row.findViewById(R.id.btn_delete);

            row.setTag(clipboard);
        } else {
            clipboard =(ClipboardDetails) row.getTag();
        }

        ClipHistory clipDetails = clipContents.get(position);
        clipboard.deviceTitleTxtView.setText(clipDetails.getDeviceName());
        clipboard.clipboardContentTxtView.setText(clipDetails.getClipContent());

       clipboard.delBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(context, "Delete button Clicked", Toast.LENGTH_LONG).show();
               ClipHistory clipHistory = clipContents.get(position);
               ClipHistoryStore storeObject = new ClipHistoryStore();
               Map<String,ClipHistory> map = storeObject.getClipContents();
               String mapKey = clipHistory.getTimestamp();
               map.remove(mapKey);
               clipContents.remove(position);

               DatabaseReference dbReference = fdb.getReference(key).child(mapKey);
               dbReference.removeValue();

               notifyDataSetChanged();
           }
       });



        return row;

    }




    static class ClipboardDetails {
        TextView deviceTitleTxtView;
        TextView clipboardContentTxtView;
        ImageButton delBtn;
    }
}
