package com.example.fooddonation.adminPart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddonation.MainActivity;
import com.example.fooddonation.R;
import com.example.fooddonation.waitingPage;
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

public class adminLogin extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView signup;
    DatabaseReference db;
    AlertDialog.Builder builder;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextAdminEmailAddress);
        password = findViewById(R.id.editTextAdminPassword);
        signup = findViewById(R.id.textView5);

        login = findViewById(R.id.adminLoginBtn);

        builder = new AlertDialog.Builder(this);

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

                            if(logintypestr.equals("admin")){
                                Intent login = new Intent(adminLogin.this, adminMainActivity.class);
                                login.putExtra("userType", "admin");
                                login.putExtra("source", "adminPannel");
                                startActivity(login);
                                finish();
                            } else {
                                FirebaseAuth.getInstance().signOut();
                                builder.setMessage("No records in admin for entered credentials")
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
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstr = email.getText().toString().trim();
                String passstr =  password.getText().toString().trim();

                if(emailstr.isEmpty()){
                    email.setError("Enter Email ID");
                    email.requestFocus();
                } else if (passstr.isEmpty()) {
                    password.setError("Enter Password");
                    password.requestFocus();
                } else if (!(emailstr.isEmpty() && passstr.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(emailstr, passstr)
                            .addOnCompleteListener(adminLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent login = new Intent(adminLogin.this, adminMainActivity.class);
                                startActivity(login);
                                finish();
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
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Contact system admin for new account", Toast.LENGTH_SHORT).show();
            }
        });
    }
}