package com.example.fooddonation.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddonation.MainActivity;
import com.example.fooddonation.R;
import com.example.fooddonation.landingPage;
import com.example.fooddonation.waitingPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signUp extends AppCompatActivity {

    EditText name, phone, address, email, pass, confirm;
    Button signup;
    TextView signin;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mFirebaseAuth = FirebaseAuth.getInstance();

        Intent type = getIntent();
        String typestr = type.getStringExtra("type");

        name = findViewById(R.id.editTextText3_0);
        phone = findViewById(R.id.editText3_1);
        address = findViewById(R.id.editTextText3_2);
        email = findViewById(R.id.editText3_3);
        pass = findViewById(R.id.password3_5);
        confirm = findViewById(R.id.confirm3_5);
        signup = findViewById(R.id.button6);
        signin = findViewById(R.id.textView17);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namestr = name.getText().toString().trim();
                String phonestr = phone.getText().toString().trim();
                String addresstr = address.getText().toString().trim();
                String emailstr = email.getText().toString().trim();
                String passstr = pass.getText().toString().trim();
                String confirmstr = confirm.getText().toString().trim();

                if (emailstr.isEmpty()) {
                    email.setError("Enter Email ID");
                    email.requestFocus();
                } else if (passstr.isEmpty()) {
                    pass.setError("Enter Password");
                    pass.requestFocus();
                } else if (confirmstr.isEmpty()) {
                    confirm.setError("Confirm Password");
                    confirm.requestFocus();
                }
                else if (emailstr.isEmpty() && passstr.isEmpty() && confirmstr.isEmpty() && namestr.isEmpty() && phonestr.isEmpty() && addresstr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter all the data", Toast.LENGTH_SHORT).show();
                } else if (!(emailstr.isEmpty() && passstr.isEmpty() && confirmstr.isEmpty())) {
                    if(passstr.equals(confirmstr)) {
                        mFirebaseAuth.createUserWithEmailAndPassword(emailstr, passstr).addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Sign Up Unsuccessful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(signUp.this, waitingPage.class);
                                    intent.putExtra("source", "signup");
                                    intent.putExtra("type", typestr);
                                    intent.putExtra("name", namestr);
                                    intent.putExtra("phone", phonestr);
                                    intent.putExtra("address", addresstr);
                                    intent.putExtra("email", emailstr);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords dont match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signin = new Intent(signUp.this, MainActivity.class);
                startActivity(signin);
                finish();
            }
        });
    }
}