package jai.clipboard;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pramod.firebase.R;
import com.pramod.firebase.storage.ClipHistory;
import com.pramod.firebase.storage.ClipHistoryStore;
import com.pramod.firebase.util.KeyStore;
import com.pramod.firebase.util.RDBHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClipboardDetails extends Fragment{

    ListView listView;
    ClipboardAdapter adapter;
    ArrayList<ClipHistory> clipboard_contents = new ArrayList<ClipHistory>();
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();

    public ClipboardDetails() {

    }

    private static String key = KeyStore.getClipboardHistoryKeyForUser();
   /* void setupElements() {
        ClipHistoryStore clipHistoryStore = new ClipHistoryStore();
        ClipHistory clipHistory = new ClipHistory("Sumsung","abcd","text",Calendar.getInstance().getTime().toString());
        ClipHistory clipHistory2 = new ClipHistory("OnePlus","helloWorld","text",Calendar.getInstance().getTime().toString());
        clipHistoryStore.addClipHistory(clipHistory);
        clipHistoryStore.addClipHistory(clipHistory2);
        RDBHandler.getInstance().write(key, clipHistoryStore.getClipContents());
    }*/

    void getElements(){
        DatabaseReference dbReference = fdb.getReference(key);

        dbReference.orderByKey().limitToLast(5).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                ClipHistory clip = new ClipHistory((Map<String, String>) dataSnapshot.getValue());
                if(clipboard_contents.size() >= 5){
                    clipboard_contents.remove(4);
                }
                clipboard_contents.add(clip);
                Collections.sort(clipboard_contents);
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            getElements();

            View view = inflater.inflate(R.layout.fragment_clipboard_details, container, false);

            adapter = new ClipboardAdapter(getActivity(), R.layout.clipboard_list, clipboard_contents);
            listView = (ListView) view.findViewById(R.id.list_clipboard_contents);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Jai","this is clickable");
                }
            });
            return view;

    }

}
