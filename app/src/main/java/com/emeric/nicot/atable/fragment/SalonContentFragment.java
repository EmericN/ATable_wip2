package com.emeric.nicot.atable.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.emeric.nicot.atable.R;
import com.emeric.nicot.atable.SalonActivity;
import com.emeric.nicot.atable.adapter.CustomAdapterSalon;
import com.emeric.nicot.atable.models.FirebaseSalonAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SalonContentFragment extends Fragment {
    public ArrayList<FirebaseSalonAdmin> salonAdmin;
    public ArrayList<FirebaseSalonAdmin> salonIdAdmin;
    public ArrayList<FirebaseSalonAdmin> salonMembre;
    public ArrayList<FirebaseSalonAdmin> salonIdMembre;
    ListView LV;
    String mail;
    CustomAdapterSalon adapter;
    private FirebaseAuth mAuth;
    private String userId, ts, TAG = "debug firestore";
    private Long tsLong;
    private FirebaseFirestore mFirestore;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        tsLong = System.currentTimeMillis();
        ts = tsLong.toString();
        salonAdmin = new ArrayList<>();
        salonIdAdmin = new ArrayList<>();
        salonMembre = new ArrayList<>();
        CollectionReference docRef = mFirestore.collection("chats");

        View v = inflater.inflate(R.layout.tab_salon_list, null);
        FloatingActionButton floatAdd = (FloatingActionButton) v.findViewById(R.id.FloatButtonAdd);
        LV = (ListView) v.findViewById(R.id.ListView);


        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userId = user.getUid();

        } else {

        }

        adapter = new CustomAdapterSalon(getContext(), R.layout.list_item, salonAdmin, salonMembre);
        LV.setAdapter(adapter);
// GET all admin rooms

        docRef.whereEqualTo("admin", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        Log.d(TAG, document.getId() + " => " + document.get("nom"));
                        String salonAdm = (String) document.get("nom");
                        String salonIdAdm = document.getId();

                        FirebaseSalonAdmin addedSalonAdmin = new FirebaseSalonAdmin(salonAdm, salonIdAdm);
                        // FirebaseSalonAdmin addedSalonIdAdmin = new FirebaseSalonAdmin(salonIdAdm);

                        salonAdmin.add(addedSalonAdmin);
                        //  salonIdAdmin.add(addedSalonIdAdmin);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d(TAG, "Error getting admin rooms : ", task.getException());
                }
            }
        });
// GET all membre rooms
        docRef.whereEqualTo("membres", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        Log.d(TAG, document.getId() + " => " + document.get("nom"));
                        String salonMemb = (String) document.get("nom");
                        String salonIdMemb = document.getId();
                        FirebaseSalonAdmin addedSalonMembre = new FirebaseSalonAdmin(salonMemb, salonIdMemb);
                        salonMembre.add(addedSalonMembre);
                        salonAdmin.addAll(salonMembre);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d(TAG, "Error getting membre rooms : ", task.getException());
                }
            }
        });



        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(getContext(), SalonActivity.class);
                FirebaseSalonAdmin PossalonAdmin = salonAdmin.get(position);
                i.putExtra("NomSalon", PossalonAdmin.getSalon());
                i.putExtra("SalonId", PossalonAdmin.getSalonId());
                i.putExtra("userId", userId);
                if (position < salonAdmin.size()) {
                    i.putExtra("tag", 1);
                } else {
                    i.putExtra("tag", 2);
                }
                startActivity(i);
            }
        });

        floatAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                final EditText edittext = new EditText(v.getContext());
                alert.setTitle("Nom du salon");
                alert.setView(edittext);
                alert.setPositiveButton("Créer", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String nomsalon = edittext.getText().toString();

                        Map<String, Object> chatsMap = new HashMap<>();
                        chatsMap.put("nom", nomsalon);
                        chatsMap.put("admin", userId);
                        chatsMap.put("membres", "");
                        chatsMap.put("pending", "");

                        mFirestore.collection("chats").document().set(chatsMap);
                        FirebaseSalonAdmin salonAdd = new FirebaseSalonAdmin(nomsalon, "");
                        salonAdmin.add(salonAdd);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });

                alert.setNegativeButton("Quitter", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                edittext.setLayoutParams(lp);
                alert.setView(edittext);
                final AlertDialog dialog = alert.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(false);

                edittext.addTextChangedListener(new TextWatcher() {
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                    }

                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(s)) {
                            dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                });
            }
        });

        return v;
    }
}
