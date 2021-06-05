package com.example.fooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fooddonation.auth.login;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editProfile extends AppCompatActivity {
    TextInputLayout nameL, phoneL, addrL;
    EditText name, phone, addr;
    Button ok;
    DatabaseReference db;
    com.example.fooddonation.auth.login login;
    String UID, namestr, phonestr, addressstr, emailstr, logintypestr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        login = new login();
        try {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e) {
            Intent home = new Intent(editProfile.this, MainActivity.class);
            startActivity(home);
            finish();
        }

        nameL = findViewById(R.id.name);
        phoneL = findViewById(R.id.phone);
        addrL = findViewById(R.id.address);

        ok = findViewById(R.id.button4);

        name = findViewById(R.id.textInputEditText);
        phone = findViewById(R.id.editTextPhone);
        addr = findViewById(R.id.editTextTextPostalAddress);

        Toast.makeText(getApplicationContext(), UID, Toast.LENGTH_SHORT).show();
        db = FirebaseDatabase.getInstance().getReference().child(UID);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    namestr = snapshot.child("name").getValue().toString();
                    phonestr = snapshot.child("phone").getValue().toString();
                    addressstr = snapshot.child("address").getValue().toString();
                    emailstr = snapshot.child("email").getValue().toString();
                    logintypestr = snapshot.child("loginType").getValue().toString();

                    name.setText(namestr);
                    phone.setText(phonestr);
                    addr.setText(addressstr);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nameL.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();
                name.setEnabled(true);
                name.setSelection(name. getText(). length());
                name.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        phoneL.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();
                phone.setEnabled(true);
                phone.setSelection(phone. getText(). length());
                phone.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        addrL.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();
                addr.setEnabled(true);
                addr.setSelection(addr. getText(). length());
                addr.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(addr, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_new_str = name.getText().toString().trim();
                String phone_new_str = phone.getText().toString().trim();
                String addr_new_str = addr.getText().toString().trim();

                login.setName(name_new_str);
                login.setPhone(phone_new_str);
                login.setAddress(addr_new_str);
                login.setLoginType(logintypestr);
                login.setEmail(emailstr);

                db.setValue(login);

                name.setEnabled(false);
                phone.setEnabled(false);
                addr.setEnabled(false);
            }
        });
    }
}