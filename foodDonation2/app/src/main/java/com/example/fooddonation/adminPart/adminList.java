package com.example.fooddonation.adminPart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddonation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class adminList extends AppCompatActivity {

    Button back;
    DatabaseReference db;
    String type;
    int countstr, retVal;
    ListView volList;
    ProgressBar loading;
    TextView who;
    ArrayList<String> volunteerList, uidlist;
    ArrayList<Integer> indexList;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        Intent listType = getIntent();
        type = listType.getStringExtra("Type");

        loading = findViewById(R.id.progressBarAdmin);
        who = findViewById(R.id.textView20Admin);

        volunteerList = new ArrayList<>();
        uidlist = new ArrayList<>();
        indexList = new ArrayList<>();

        volList = findViewById(R.id.volListAdmin);
        registerForContextMenu(volList);

        back = findViewById(R.id.backAdmin);

        db = FirebaseDatabase.getInstance().getReference();

        Query query;
        try {
            switch (type) {
                case "Volunteer List":
                    who.setText("Volunteers");
                    query = db.orderByChild("loginType").equalTo("Volunteer");
                    retVal = execute(query, "Volunteer");
                    loadStop();
                    break;
                case "Donors List":
                    who.setText("Donors");
                    query = db.orderByChild("loginType").equalTo("Donor");
                    retVal = execute(query, "Donor");
                    loadStop();
                    break;
                case "Pending Pickups":
                    who.setText("Pending Pickups");
                    query = db.orderByChild("loginType").equalTo("Donor");
                    retVal = execute(query, "Pending");
                    loadStop();
                    break;
                case "Completed Pickups":
                    who.setText("Completed Pickups");
                    query = db.orderByChild("loginType").equalTo("Donor");
                    retVal = execute(query, "Completed");
                    loadStop();
                    break;
                default:
                    query = db.orderByChild("loginType").equalTo("None");
                    retVal = execute(query, "None");
            }
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "Select an option", Toast.LENGTH_SHORT).show();
            Intent getType = new Intent(adminList.this, adminMainActivity.class);
            startActivity(getType);
            finish();
        }

        volList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                for(int j = 0; j< volunteerList.size(); j++){
                    if(volunteerList.get(j).equals(selectedItem)){
                        String volUid = uidlist.get(j);
                        Intent disp = new Intent(adminList.this, adminDetailsList.class);
                        disp.putExtra("name", volUid);
                        disp.putExtra("type", type);
                        disp.putExtra("index", String.valueOf(indexList.get(i)));
                        startActivity(disp);
                        finish();
                    } else{
//                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(adminList.this, adminMainActivity.class);
                startActivity(back);
            }
        });
    }

    public int execute(Query query, String type){
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String volName = ds.child("name").getValue().toString();
                        String volUidStr = ds.getKey();

                        countstr = (int) (ds.getChildrenCount() - 5);

                        if(type.equals("Completed")) {
                            for(int i=1; i<=countstr; i++) {
                                String picked = ds.child(String.valueOf(i)).child("picked").getValue().toString();
                                String number = ds.child(String.valueOf(i)).child("platesno").getValue().toString();
                                if (picked.equals("Yes")) {
                                    volunteerList.add(volName+" - "+number+" plates");
                                    uidlist.add(volUidStr);
                                    indexList.add(i);
                                }
                            }
                        } else if(type.equals("Pending")){
                            for(int i=1; i<=countstr; i++) {
                                String picked = ds.child(String.valueOf(i)).child("picked").getValue().toString();
                                String number = ds.child(String.valueOf(i)).child("platesno").getValue().toString();
                                if (picked.equals("No")) {
                                    volunteerList.add(volName+" - "+number+" plates");
                                    uidlist.add(volUidStr);
                                    indexList.add(i);
                                }
                            }
                        } else if(type.equals("Volunteer") || type.equals("Donor")) {
                            volunteerList.add(volName);
                            uidlist.add(volUidStr);
                            indexList.add(0);
                        }
                    }

                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, volunteerList);
                    if(volunteerList.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No Entries", Toast.LENGTH_SHORT).show();
                    }
                    volList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return 1;
    }

    public void loadStop(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(retVal==1) {
                    loading.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    Intent back = new Intent(adminList.this, adminMainActivity.class);
                    startActivity(back);
                }
            }
        },1500);
    }
}