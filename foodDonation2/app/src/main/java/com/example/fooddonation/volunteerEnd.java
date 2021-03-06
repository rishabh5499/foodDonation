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

public class volunteerEnd extends AppCompatActivity {

    Button logOut;
    DatabaseReference db;
    String UID;
    int countstr;
    ListView donList;
    ArrayList<String> donorlist, uidlist;
    ArrayList<Integer> indexList;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_end);

        donorlist = new ArrayList<>();
        uidlist = new ArrayList<>();
        indexList = new ArrayList<>();
        donList = findViewById(R.id.donList);
        registerForContextMenu(donList);
        try {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        logOut = findViewById(R.id.button9);

        db = FirebaseDatabase.getInstance().getReference();

        Query query = db.orderByChild("loginType").equalTo("Donor");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String volName = ds.child("name").getValue().toString();
                        String volUid = ds.getKey();
                        countstr = (int) (ds.getChildrenCount()-5);

                        for(int i=1; i<=countstr; i++) {
                            String volNeed = ds.child(String.valueOf(i)).child("volunteerNeed").getValue().toString();
                            String number = ds.child(String.valueOf(i)).child("platesno").getValue().toString();

                            if(volNeed.equals("Yes")) {
                                donorlist.add(volName+" - "+number+" plates");
                                uidlist.add(volUid);
                                indexList.add(i);
                            }
                        }
                    }
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, donorlist);
                    donList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        donList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                for(int j = 0; j< donorlist.size(); j++){
                    if(donorlist.get(j).equals(selectedItem)){
                        String volUid = uidlist.get(j);
                        goAhead(volUid, indexList.get(i));
                    } else{
//                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(volunteerEnd.this, MainActivity.class);
                startActivity(intToMain);
                finish();
            }
        });
    }
    public void goAhead(String volUid, int i){
        Intent disp = new Intent(volunteerEnd.this, detailsList.class);
        disp.putExtra("src", "VolunteerPage");
        disp.putExtra("name", volUid);
        disp.putExtra("index", String.valueOf(i));
        startActivity(disp);
    }
}