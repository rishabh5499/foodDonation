package com.example.fooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detailsList extends AppCompatActivity {

    EditText name, email, phone, address, platesNu, fdType;
    TextView plates, addr, foodTpe;
    TextInputLayout emailBtn, phoneBtn, platesNum, foodType;
    DatabaseReference db;
    details details;
    Button home, edit;
    String namestr, phonestr, addressstr, emailstr;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);

        plates = findViewById(R.id.textView23);
        addr = findViewById(R.id.textView15Admin);
        foodTpe = findViewById(R.id.textView14);

        details = new details();

        platesNu = findViewById(R.id.platsNum);
        platesNum = findViewById(R.id.platesNumber);
        name = findViewById(R.id.editTextVolNameAdmin);
        email = findViewById(R.id.editTextVolEmailAdmin);
        phone = findViewById(R.id.editTextVolPhoneAdmin);
        address = findViewById(R.id.editTextVolAddressAdmin);
        foodType = findViewById(R.id.VolFoodType);
        fdType = findViewById(R.id.editTextFoodType);

        home = findViewById(R.id.buttonHomeAdmin);
        edit = findViewById(R.id.button8);

        emailBtn = findViewById(R.id.VolEmailAdmin);
        phoneBtn = findViewById(R.id.VolPhoneAdmin);

        Intent volUid = getIntent();
        String volUidStr = volUid.getStringExtra("name");
        String srcStr = volUid.getStringExtra("src");
        String index = volUid.getStringExtra("index");

        if(srcStr.equals("DonorPage")) {
            platesNu.setVisibility(View.INVISIBLE);
            plates.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            foodType.setVisibility(View.INVISIBLE);
            fdType.setVisibility(View.INVISIBLE);
            foodTpe.setVisibility(View.INVISIBLE);
        } else if(srcStr.equals("VolunteerPage")){
            platesNu.setVisibility(View.VISIBLE);
            plates.setVisibility(View.VISIBLE);
            platesNum.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            foodType.setVisibility(View.VISIBLE);
            fdType.setVisibility(View.VISIBLE);
            foodTpe.setVisibility(View.VISIBLE);
            addr.setText("Locality");
        }

        db = FirebaseDatabase.getInstance().getReference().child(volUidStr);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    namestr = snapshot.child("name").getValue().toString();
                    phonestr = snapshot.child("phone").getValue().toString();
                    addressstr = snapshot.child("address").getValue().toString();
                    emailstr = snapshot.child("email").getValue().toString();

                    name.setText(namestr);
                    phone.setText(phonestr);
                    email.setText(emailstr);

                    if(srcStr.equals("VolunteerPage")){
                        String locality = snapshot.child(index).child("locality").getValue().toString();
                        String number = snapshot.child(index).child("platesno").getValue().toString();
                        String picked = snapshot.child(index).child("picked").getValue().toString();
                        String foodType = snapshot.child(index).child("foodType").getValue().toString();

                        platesNu.setText(number);
                        address.setText(locality);
                        fdType.setText(foodType);
                        if(picked.equals("Yes")) {
                            edit.setEnabled(false);
                            edit.setText("Picked Up");
                        } else if(picked.equals("No")){
                            edit.setText("Unpicked");
                        }
                    } else {
                        address.setText(addressstr);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(srcStr.equals("DonorPage")) {
                    Intent home = new Intent(detailsList.this, donationDetails.class);
                    startActivity(home);
                } else if(srcStr.equals("VolunteerPage")){
                    Intent home = new Intent(detailsList.this, volunteerEnd.class);
                    startActivity(home);
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String locality = snapshot.child(index).child("locality").getValue().toString();
                        String number = snapshot.child(index).child("platesno").getValue().toString();
                        String picked = snapshot.child(index).child("picked").getValue().toString();
                        String volunteerNeed = snapshot.child(index).child("volunteerNeed").getValue().toString();
                        String foodType = snapshot.child(index).child("foodType").getValue().toString();

                        details.setLocality(locality);
                        details.setPlatesno(number);
                        details.setVolunteerNeed(volunteerNeed);
                        details.setFoodType(foodType);
                        if(picked.equals("No")){
                            details.setPicked("Yes");
                            edit.setText("Picked Up");
                        }
                        db.child(index).setValue(details);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        phoneBtn.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

        emailBtn.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto: "+emailstr));
                startActivity(email);
            }
        });
    }

    private void makeCall(){
        if(phonestr.trim().length()>0){
            if(ContextCompat.checkSelfPermission(detailsList.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(detailsList.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                Intent phoneInt = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonestr));
                startActivity(phoneInt);
            }
        } else {
            phonestr = phone.getText().toString().trim();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makeCall();
            } else{
                Toast.makeText(getApplicationContext(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}