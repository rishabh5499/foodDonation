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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detailsList extends AppCompatActivity {

    EditText name, email, phone, address;
    TextInputLayout emailBtn, phoneBtn;
    DatabaseReference db;
    Button home;
    String namestr, phonestr, addressstr, emailstr;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);

        name = findViewById(R.id.editTextVolName);
        email = findViewById(R.id.editTextVolEmail);
        phone = findViewById(R.id.editTextVolPhone);
        address = findViewById(R.id.editTextVolAddress);
        home = findViewById(R.id.buttonHome);

        emailBtn = findViewById(R.id.VolEmail);
        phoneBtn = findViewById(R.id.VolPhone);

        Intent volUid = getIntent();
        String volUidStr = volUid.getStringExtra("name");
        String srcStr = volUid.getStringExtra("src");

        db = FirebaseDatabase.getInstance().getReference().child(volUidStr);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    namestr = snapshot.child("name").getValue().toString();
                    phonestr = snapshot.child("phone").getValue().toString();
                    addressstr = snapshot.child("address").getValue().toString();
                    emailstr = snapshot.child("email").getValue().toString();

                    name.setText(namestr);
                    phone.setText(phonestr);
                    address.setText(addressstr);
                    email.setText(emailstr);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
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