package com.pramod.firebase;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {


    public Settings() {
        // Required empty public constructor
    }


    EditText ed, oldpass;
    Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ed = view.findViewById(R.id.editPassword);
        oldpass=view.findViewById(R.id.old_pass);
        btn = view.findViewById(R.id.btnChangePass);
        setup(ed,oldpass,btn);
        return view;
    }

    void setup(final EditText ed,final EditText oldpass ,final Button btn ) {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = ed.getText().toString();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldpass.getText().toString());

                    user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(text).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("update", "Password updated");
                                                Toast.makeText(getActivity().getApplicationContext(), "Password changed successful!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d("not update", "Error password not updated");
                                            }
                                        }
                                    });
                                } else {
                                    Log.d("fail", "Error auth failed");
                                }
                            }
                        });
            }
        });
    }
}
