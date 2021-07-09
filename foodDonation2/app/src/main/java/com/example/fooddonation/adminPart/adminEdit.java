package com.example.fooddonation.adminPart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fooddonation.R;
import com.example.fooddonation.details;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminEdit extends AppCompatActivity {

    EditText name, email, phone, address;
    TextInputLayout emailBtn, phoneBtn;
    DatabaseReference db;
    com.example.fooddonation.details details;
    Button done, pickToggle;
    String namestr, phonestr, addressstr, emailstr, pickedstr, localitystr, platesnum, volunteerneed;
    private static final int REQUEST_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);

        details = new details();

        name = findViewById(R.id.editTextVolNameEditAdmin);
        email = findViewById(R.id.editTextVolEmailEditAdmin);
        phone = findViewById(R.id.editTextVolPhoneEditAdmin);
        address = findViewById(R.id.editTextVolAddressEditAdmin);

        done = findViewById(R.id.buttonHomeEditAdmin);
        pickToggle = findViewById(R.id.button3Admin);

        emailBtn = findViewById(R.id.VolEmailEditAdmin);
        phoneBtn = findViewById(R.id.VolPhoneEditAdmin);

        Intent volUid = getIntent();
        String volUidStr = volUid.getStringExtra("name");
        String index = volUid.getStringExtra("index");
        String type = volUid.getStringExtra("type");

        if(type.equals("Completed Pickups")) {
            pickToggle.setText("Mark as Unpicked");
        } else if(type.equals("Pending Pickups")) {
            pickToggle.setText("Mark as Picked");
        }
        db = FirebaseDatabase.getInstance().getReference().child(volUidStr);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    namestr = snapshot.child("name").getValue().toString();
                    phonestr = snapshot.child("phone").getValue().toString();
                    addressstr = snapshot.child("address").getValue().toString();
                    emailstr = snapshot.child("email").getValue().toString();

                    pickedstr = snapshot.child(index).child("picked").getValue().toString();
                    localitystr = snapshot.child(index).child("locality").getValue().toString();
                    volunteerneed = snapshot.child(index).child("volunteerNeed").getValue().toString();
                    platesnum = snapshot.child(index).child("platesno").getValue().toString();

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

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(adminEdit.this, adminMainActivity.class);
                startActivity(home);
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
                email.setData(Uri.parse("mailto: " + emailstr));
                startActivity(email);
            }
        });

        pickToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setLocality(localitystr);
                details.setPlatesno(platesnum);
                details.setVolunteerNeed(volunteerneed);

                if(type.equals("Completed Pickups")) {
                    details.setPicked("No");
                    db.child(index).setValue(details);
                    Toast.makeText(getApplicationContext(), "Item marked as unpicked", Toast.LENGTH_SHORT).show();
                } else if(type.equals("Pending Pickups")){
                    details.setPicked("Yes");
                    db.child(index).setValue(details);
                    Toast.makeText(getApplicationContext(), "Item marked as picked", Toast.LENGTH_SHORT).show();
                }
                Intent back = new Intent(adminEdit.this, adminMainActivity.class);
                startActivity(back);
                finish();
            }
        });
    }

    private void makeCall(){
        if(phonestr.trim().length()>0){
            if(ContextCompat.checkSelfPermission(adminEdit.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(adminEdit.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
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