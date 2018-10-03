package com.emeric.nicot.atable.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.emeric.nicot.atable.R;
import com.emeric.nicot.atable.adapter.CustomAdapterNotif;
import com.emeric.nicot.atable.models.FirebaseSalonRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class CreativeContentFragment extends Fragment {

    private String TAG = "debug Creative TAB";
    private TextView textViewCreativeTitle, textViewCreativeTitle2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_creative, null);
        textViewCreativeTitle = v.findViewById(R.id.textViewCreativeTitle);
        textViewCreativeTitle2 = v.findViewById(R.id.textViewCreativeTitle2);

        textViewCreativeTitle.setText("Créer ton propre Sticker !");
        textViewCreativeTitle2.setText("Choisi comment le créer ...");

        return v;
    }
}