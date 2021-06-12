package com.example.fooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class donorEnd extends AppCompatActivity {

    Button back;
    DatabaseReference db;
    String UID;
    details details;
    ListView volList;
    ArrayList<String> volunteerList, uidlist;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_end);

        volunteerList = new ArrayList<>();
        uidlist = new ArrayList<>();
        volList = findViewById(R.id.volList);
        registerForContextMenu(volList);
        try {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        details = new details();

        back = findViewById(R.id.back);

        db = FirebaseDatabase.getInstance().getReference();

        Query query = db.orderByChild("loginType").equalTo("Volunteer");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String volName = ds.child("name").getValue().toString();
                        String volUidStr = ds.getKey();
                        volunteerList.add(volName);
                        uidlist.add(volUidStr);
                    }

                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, volunteerList);
                    volList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        volList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                for(int j = 0; j< volunteerList.size(); j++){
                    if(volunteerList.get(j).equals(selectedItem)){
                        String volUid = uidlist.get(j);
                        goAhead(volUid);
                    } else{
//                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(donorEnd.this, donationDetails.class);
                startActivity(back);
            }
        });
    }
    public void goAhead(String volUid){
        Intent disp = new Intent(donorEnd.this, detailsList.class);
        disp.putExtra("name", volUid);
        disp.putExtra("src", "DonorPage");
        startActivity(disp);
    }
}