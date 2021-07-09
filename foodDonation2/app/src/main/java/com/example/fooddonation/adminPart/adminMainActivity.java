package com.example.fooddonation.adminPart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddonation.MainActivity;
import com.example.fooddonation.R;
import com.google.firebase.auth.FirebaseAuth;

public class adminMainActivity extends AppCompatActivity {

    TextView tv1;
    RadioGroup rg;
    RadioButton list;
    Button go, logOut;
    int id;
    ProgressBar loading;
    String listType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        tv1 = findViewById(R.id.textViewAdmin);
        rg = findViewById(R.id.rgAdmin);
        go = findViewById(R.id.buttonAdmin);
        logOut = findViewById(R.id.appHome);

        loading = findViewById(R.id.progressBar2Admin);

        tv1.setVisibility(View.INVISIBLE);
        rg.setVisibility(View.INVISIBLE);
        go.setVisibility(View.INVISIBLE);
        logOut.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
                tv1.setVisibility(View.VISIBLE);
                rg.setVisibility(View.VISIBLE);
                go.setVisibility(View.VISIBLE);
                logOut.setVisibility(View.VISIBLE);
            }
        },1500);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = rg.getCheckedRadioButtonId();
                list = findViewById(id);
                try {
                    listType = list.getText().toString();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Select an option", Toast.LENGTH_SHORT).show();
                }
                Intent list = new Intent(adminMainActivity.this, adminList.class);
                list.putExtra("Type", listType);
                startActivity(list);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent home = new Intent(adminMainActivity.this, MainActivity.class);
                startActivity(home);
                finish();
            }
        });
    }
}