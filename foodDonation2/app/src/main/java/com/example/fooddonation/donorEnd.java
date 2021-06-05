package com.example.fooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

public class donorEnd extends AppCompatActivity {

    EditText organization, volName, volPhone;
    Button back;
    DatabaseReference db;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_end);

        organization = findViewById(R.id.organization);
        volName = findViewById(R.id.volName);
        volPhone = findViewById(R.id.volPhone);
    }
}