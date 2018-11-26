package com.shweta.shareFile;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.google.firebase.database.core.Tag;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pramod.firebase.Constants;
import com.pramod.firebase.clipboard.ClipboardHandler;
import com.pramod.firebase.services.ClipboardMonitorService;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.File;
import java.io.IOException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.pramod.firebase.storage.ClipHistory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;


public class FirebaseFileHandler extends AppCompatActivity {

    static FirebaseUser user;
    static Uri filePath = null;

    private final String CHANNEL_ID = "Notification";
    private final int NOTIFICATION_ID = 001;

    private static FirebaseStorage storage;


    public static void sendIntentHandler(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        }
    }

    public static void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {

        }
    }

    public static void setupFirebaseStorage() {

        storage = FirebaseStorage.getInstance();
    }



    public static void handleSendImage(Intent intent) {

        setupFirebaseStorage();


        //get firebase authenticated user
        user = FirebaseAuth.getInstance().getCurrentUser();


        filePath = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        //FirebaseStorage storage;

        //final ProgressDialog progressDialog = new ProgressDialog(this);
        //progressDialog.setTitle("Uploading");
        // progressDialog.show();

        //storage = FirebaseStorage.getInstance();

        // [START upload_create_reference]
        // Create a storage reference from our app
         StorageReference storageRef = storage.getReference();


        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
         final StorageReference imageRef = storageRef.child("images/" + user.getUid() + ".jpg");
        imageRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i(Constants.TAG, "onSuccess file upload: uri= "+ uri.toString());
                                //downloadFile2(uri.toString());
                                ClipboardMonitorService.saveInFirebase(uri.toString(),Constants.TYPE_IMAGE);
                            }
                        });
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        //Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        // progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });


    }



    /*public void displayNotification() {

        //createNotificationChannel();

        Intent LandingIntent = new Intent(this, LoginActivity.class);
        LandingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent landingPendingIntent = PendingIntent.getActivity(this, 0, LandingIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        mBuilder.setContentTitle("CloudCopy");
        mBuilder.setContentText("You have new notification");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setContentIntent(landingPendingIntent);
        mBuilder.setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/



    public static void downloadFile(String clipBoardContent) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(clipBoardContent);
        //final StorageReference imageRef = storageRef.child(user.getUid());

        String DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File rootPath = new File(DOWNLOAD_DIR);
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }


        try {
            final File localFile = new File(DOWNLOAD_DIR, "swethaimage.jpg");
            localFile.createNewFile();
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.e("firebase ", ";local tem file created  created ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("firebase ", ";local tem file not created  created " + exception.toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
