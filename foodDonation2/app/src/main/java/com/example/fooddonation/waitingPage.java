package com.example.fooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddonation.auth.login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class waitingPage extends AppCompatActivity {

    String UID;
    DatabaseReference db;
    com.example.fooddonation.auth.login login;
    String typestr, namestr, phonestr, addresstr, emailstr, sourcestr, userstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_page);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(waitingPage.this, DonationPage.class);
                startActivity(intent);
                finish();
            }
        },1000);

        login = new login();

        try {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (Exception e){
            Intent home = new Intent(waitingPage.this, MainActivity.class);
            startActivity(home);
            finish();
        }

        Intent src = getIntent();
        sourcestr = src.getStringExtra("source");
        userstr = src.getStringExtra("userType");
        db = FirebaseDatabase.getInstance().getReference();

        if (sourcestr!= null && sourcestr.equals("signup")) {
            getIntents();
        } else {}

    }

    private void getIntents(){
        Intent type = getIntent();
        typestr = type.getStringExtra("type");
        namestr = type.getStringExtra("name");
        phonestr = type.getStringExtra("phone");
        addresstr = type.getStringExtra("address");
        emailstr = type.getStringExtra("email");

        login.setLoginType(typestr);
        login.setName(namestr);
        login.setPhone(phonestr);
        login.setAddress(addresstr);
        login.setEmail(emailstr);

        db.child(UID).setValue(login);
    }
}