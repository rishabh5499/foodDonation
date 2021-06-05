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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddonation.auth.signUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText email, pass;
    TextView signup;
    Button login, ok;
    Switch type;
    String typestr;
    AlertDialog.Builder builder;
    DatabaseReference db;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText);
        pass = (EditText)findViewById(R.id.password4);
        signup = findViewById(R.id.textView11);
        login = findViewById(R.id.button5);
        ok = findViewById(R.id.button3);
        type = findViewById(R.id.switch1);

        builder = new AlertDialog.Builder(this);

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typestr=" ";
                if(type.isChecked()){
                    type.setText("Volunteer");
                    typestr = "Volunteer";
                    ok.setText("Volunteer");
                } else {
                    type.setText("Donor");
                    typestr = "Donor";
                    ok.setText("Donor");
                }
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    db = FirebaseDatabase.getInstance().getReference().child(uid);

                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String logintypestr = snapshot.child("loginType").getValue().toString();
                            Toast.makeText(getApplicationContext(), typestr, Toast.LENGTH_SHORT).show();

                            if(logintypestr.equals(typestr)){
                                Intent login = new Intent(MainActivity.this, waitingPage.class);
                                login.putExtra("userType", typestr);
                                startActivity(login);
                                finish();
                                Toast.makeText(getApplicationContext(), "Welcome. Will add proper page in next version", Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseAuth.getInstance().signOut();
//                                Toast.makeText(getApplicationContext(), "No records for "+typestr+" with entered email", Toast.LENGTH_SHORT).show();
                                builder.setMessage("No records in "+typestr+" for entered credentials")
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {

                }
            }
        };

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setEnabled(true);
                pass.setEnabled(true);
                login.setEnabled(true);
                signup.setEnabled(true);
                if (!type.isChecked()){
                    typestr = "Donor";
                } else{
                    typestr = "Volunteer";
                }
                Toast.makeText(getApplicationContext(), typestr, Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailstr = email.getText().toString().trim();
                String passstr =  pass.getText().toString().trim();

                if(emailstr.isEmpty()){
                    email.setError("Enter Email ID");
                    email.requestFocus();
                } else if (passstr.isEmpty()) {
                    pass.setError("Enter Password");
                    pass.requestFocus();
                } else if (emailstr.isEmpty() && passstr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
                } else if (!(emailstr.isEmpty() && passstr.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(emailstr, passstr).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(MainActivity.this, signUp.class);
                signup.putExtra("type", typestr);
                startActivity(signup);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}