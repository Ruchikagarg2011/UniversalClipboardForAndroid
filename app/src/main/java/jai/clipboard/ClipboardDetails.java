package jai.clipboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.FirebaseDatabase;
import com.pramod.firebase.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClipboardDetails extends Fragment {

    ListView listView;
    ClipboardAdapter adapter;
    ArrayList<Clipboard>  clipboard_contents = new ArrayList<Clipboard>();

    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    

    public ClipboardDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        clipboard_contents.add(new Clipboard("D01","first copy", "text"));
        clipboard_contents.add(new Clipboard("D02","first copy", "text"));

        View view = inflater.inflate(R.layout.fragment_clipboard_details, container, false);
        adapter = new ClipboardAdapter(getActivity(), R.layout.clipboard_list,clipboard_contents);

        listView = (ListView) view.findViewById(R.id.list_clipboard_contents);
        listView.setAdapter(adapter);
        return view;
    }

}
