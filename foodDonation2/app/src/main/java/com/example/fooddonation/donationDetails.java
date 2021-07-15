package com.example.fooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class donationDetails extends AppCompatActivity {

    details details;
    DatabaseReference db;
    EditText platesno, locality, foodType;
    RadioGroup rg;
    RadioButton rb;
    Button donate, logout, editProfile, showdetails;
    AlertDialog.Builder builder;
    String UID, countstr, rbtnstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);

        details = new details();
        try {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (Exception e){
            Intent refresh = new Intent(donationDetails.this, donationDetails.class);
            startActivity(refresh);
            Toast.makeText(getApplicationContext(), "Try again",  Toast.LENGTH_SHORT).show();
        }
        try {
            db = FirebaseDatabase.getInstance().getReference().child(UID);
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    countstr = String.valueOf(snapshot.getChildrenCount() - 4);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e){
            Intent home = new Intent(donationDetails.this, MainActivity.class);
            startActivity(home);
        }

        platesno = findViewById(R.id.editTextPlates);
        locality = findViewById(R.id.editTextLocality);
        foodType = findViewById(R.id.editTextFoodType);

        builder = new AlertDialog.Builder(this);

        rg = findViewById(R.id.rgAdmin);
        donate = findViewById(R.id.donateBtn);
        showdetails = findViewById(R.id.button7);
        logout = findViewById(R.id.logOut);
        editProfile = findViewById(R.id.editProfile);

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String platestr = platesno.getText().toString().trim();
                String localitystr = locality.getText().toString().trim();
                String foodTypestr = foodType.getText().toString().trim();

                int rbtnId = rg.getCheckedRadioButtonId();

                if(rbtnId!=-1) {
                    rb = findViewById(rbtnId);
                    rbtnstr = rb.getText().toString();

                    details.setPlatesno(platestr);
                    details.setLocality(localitystr);
                    details.setVolunteerNeed(rbtnstr);
                    details.setFoodType(foodTypestr);
                    details.setPicked("No");
                    db.child(countstr).setValue(details);

                    builder.setMessage("Thank You for donating")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    builder.setMessage("Select volunteer requirement")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        showdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent end = new Intent(donationDetails.this, donorEnd.class);
                startActivity(end);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(donationDetails.this, MainActivity.class);
                startActivity(intToMain);
                finish();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(donationDetails.this, com.example.fooddonation.editProfile.class);
                startActivity(edit);
            }
        });
    }
}