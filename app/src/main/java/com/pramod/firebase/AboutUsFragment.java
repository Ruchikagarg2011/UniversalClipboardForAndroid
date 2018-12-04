package com.pramod.firebase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

public class AboutUsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aboutus, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lovely();
    }

    void lovely() {
        new LovelyStandardDialog(getActivity(), R.style.EditTextTintTheme)
                .setTopColorRes(R.color.teal)
                .setTitle("Our Team")
                .setMessage("Pramod Nanduri \n" + "Jai Karvir" + "\n" + "Ruchika Garg\n" + "Swetha Sinha")
                .setIcon(R.drawable.ic_star_border_white_36dp)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setCancelable(true)
                .show();
    }

}
