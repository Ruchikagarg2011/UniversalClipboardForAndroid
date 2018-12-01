package com.shweta.shareFile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pramod.firebase.Constants;
import com.pramod.firebase.services.ClipboardMonitorService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FirebaseFileHandler {

    FirebaseUser user;
    Uri filePath = null;

    private FirebaseStorage storage;

    private static FirebaseFileHandler INSTANCE = new FirebaseFileHandler();


    public static FirebaseFileHandler getINSTANCE() {
        return INSTANCE;
    }


    public void sendIntentHandler(Context context, Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleSendImage(context, intent, type); // Handle single image being sent
        }
    }


    public void setupFirebaseStorage() {
        storage = FirebaseStorage.getInstance();
    }


    public void handleSendImage(final Context context, final Intent intent, String type) {

        setupFirebaseStorage();

        String ext = null;

        if (type.startsWith("image/") == true) {
            ext = ".png";
        } else if (type.startsWith("application/pdf") == true) {
            ext = ".pdf";
        } else if (type.startsWith("video/") == true) {
            ext = ".mov";
        } else if (type.startsWith("audio/") == true) {
            ext = ".mp3";
        }

        //get firebase authenticated user
        user = FirebaseAuth.getInstance().getCurrentUser();

        filePath = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

        // [START upload_create_reference]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.jpg'").format(new Date());
        final StorageReference imageRef = storageRef.child("images/" + fileName);
        imageRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i(Constants.TAG, "onSuccess file upload: uri= " + uri.toString());
                                ClipboardMonitorService.saveInFirebase(uri.toString(), Constants.TYPE_IMAGE);
                            }
                        });
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        Toast.makeText(context, "Could not download. Please check you storage permission", Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void downloadFile(final Context context, String clipBoardContent) {


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(clipBoardContent);
        String DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File rootPath = new File(DOWNLOAD_DIR);
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }

        try {
            String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.jpg'").format(new Date());

            final File localFile = new File(DOWNLOAD_DIR, fileName);
            localFile.createNewFile();
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d(Constants.TAG, "Image downloaded");
                    Toast.makeText(context, "Download complete", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(Constants.TAG, "Failed image downloaded");
                    //Toast.makeText(context, "Download failed", Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            //Toast.makeText(context, "Storage exception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
