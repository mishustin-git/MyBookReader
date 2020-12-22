package com.example.mybookreader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BlankFragment extends Fragment {

    private String str;

    public BlankFragment() {
        // Required empty public constructor
    }
    public BlankFragment(String stroka)
    {
        str = stroka;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        TextView textView = getView().findViewById(R.id.textttt);
        textView.setText(str);
    }
}