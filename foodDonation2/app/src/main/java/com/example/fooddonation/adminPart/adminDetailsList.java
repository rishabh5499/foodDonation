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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminDetailsList extends AppCompatActivity {

    EditText name, email, phone, address;
    TextInputLayout emailBtn, phoneBtn;
    DatabaseReference db;
    Button home, edit;
    String namestr, phonestr, addressstr, emailstr, pickedstr;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_details_list);

        name = findViewById(R.id.editTextVolNameAdmin);
        email = findViewById(R.id.editTextVolEmailAdmin);
        phone = findViewById(R.id.editTextVolPhoneAdmin);
        address = findViewById(R.id.editTextVolAddressAdmin);

        home = findViewById(R.id.buttonHomeAdmin);
        edit = findViewById(R.id.button2Admin);

        emailBtn = findViewById(R.id.VolEmailAdmin);
        phoneBtn = findViewById(R.id.VolPhoneAdmin);

        Intent volUid = getIntent();
        String volUidStr = volUid.getStringExtra("name");
        String type = volUid.getStringExtra("type");
        String index = volUid.getStringExtra("index");

        db = FirebaseDatabase.getInstance().getReference().child(volUidStr);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    namestr = snapshot.child("name").getValue().toString();
                    phonestr = snapshot.child("phone").getValue().toString();
                    addressstr = snapshot.child("address").getValue().toString();
                    emailstr = snapshot.child("email").getValue().toString();

//                    pickedstr = snapshot.child(index).child("picked").getValue().toString();
//                    Toast.makeText(getApplicationContext(), pickedstr, Toast.LENGTH_SHORT).show();

                    name.setText(namestr);
                    phone.setText(phonestr);
                    address.setText(addressstr);
                    email.setText(emailstr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(adminDetailsList.this, adminMainActivity.class);
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
                email.setData(Uri.parse("mailto: "+emailstr));
                startActivity(email);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(adminDetailsList.this, adminEdit.class);
                edit.putExtra("name", volUidStr);
                edit.putExtra("index", index);
                edit.putExtra("type", type);
                startActivity(edit);
            }
        });
    }

    private void makeCall(){
        if(phonestr.trim().length()>0){
            if(ContextCompat.checkSelfPermission(adminDetailsList.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(adminDetailsList.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
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