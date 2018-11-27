package jai.clipboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pramod.firebase.R;
import com.pramod.firebase.storage.ClipHistory;
import com.pramod.firebase.storage.ClipHistoryStore;
import com.pramod.firebase.util.KeyStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ClipboardAdapter extends ArrayAdapter<ClipHistory> {

    Activity context;
    int layoutId;
    ArrayList<ClipHistory> clipContents = new ArrayList<ClipHistory>();
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    private static String key = KeyStore.getClipboardHistoryKeyForUser();

    public ClipboardAdapter(Activity context, int layoutId, ArrayList<ClipHistory> clipContents) {
        super(context, layoutId, clipContents);
        this.context = context;
        this.layoutId = layoutId;
        this.clipContents = clipContents;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        View row = view;

        ClipHistory clipDetails = clipContents.get(position);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutId, parent, false);

        TextView device_title_txt = (TextView) row.findViewById(R.id.device_title);
        device_title_txt.setText(clipDetails.getDeviceName());

        if(clipDetails.getMessageType().equals("1")){
            RelativeLayout single_clip_layout = row.findViewById(R.id.single_clip_layout);
            TextView clip_content_txt = new TextView(this.getContext());
            clip_content_txt.setId(R.id.clipboard_content);
            clip_content_txt.setTextSize(15);
            clip_content_txt.setText(clipDetails.getClipContent());
            clip_content_txt.setAutoSizeTextTypeWithDefaults(clip_content_txt.AUTO_SIZE_TEXT_TYPE_UNIFORM);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(400,RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.RIGHT_OF, R.id.img_device);
            lp.addRule(RelativeLayout.BELOW, R.id.device_title);

            clip_content_txt.setLayoutParams(lp);
            single_clip_layout.addView(clip_content_txt);

        }else if(clipDetails.getMessageType().equals("2")){

            RelativeLayout single_clip_layout = row.findViewById(R.id.single_clip_layout);
            final ImageView clip_content_img = new ImageView(this.getContext());
            clip_content_img.setId(R.id.clipboard_content);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(400,200);
            lp.addRule(RelativeLayout.RIGHT_OF, R.id.img_device);
            lp.addRule(RelativeLayout.BELOW, R.id.device_title);
            clip_content_img.setLayoutParams(lp);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(clipDetails.getClipContent());

            try {
                final File localFile = File.createTempFile("images", "jpg");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        clip_content_img.setImageBitmap(bitmap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            } catch (IOException e ) {}

            single_clip_layout.addView(clip_content_img);
        }



        ImageButton del_btn = (ImageButton) row.findViewById(R.id.btn_delete);

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete button Clicked", Toast.LENGTH_LONG).show();
                ClipHistory clipHistory = clipContents.get(position);
                ClipHistoryStore storeObject = new ClipHistoryStore();
                Map<String, ClipHistory> map = storeObject.getClipContents();
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

}
