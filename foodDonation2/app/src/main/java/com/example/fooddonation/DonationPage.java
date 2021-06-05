package com.example.fooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DonationPage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    details details;
    DatabaseReference db, count;
    EditText platesno, locality;
    RadioGroup rg;
    RadioButton rb;
    Button donate, logout, editProfile;
    String UID, countstr, rbtnstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        details = new details();
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_editProfile, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        count = FirebaseDatabase.getInstance().getReference();
        count.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countstr = String.valueOf(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        platesno = findViewById(R.id.editTextPlates);
        locality = findViewById(R.id.editTextLocality);

        rg = findViewById(R.id.rg);
        donate = findViewById(R.id.donateBtn);

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String platestr = platesno.getText().toString().trim();
                String localitystr = locality.getText().toString().trim();

                int rbtnId = rg.getCheckedRadioButtonId();
                rb = findViewById(rbtnId);
                rbtnstr = rb.getText().toString();

                details.setPlatesno(platestr);
                details.setLocality(localitystr);

                db = FirebaseDatabase.getInstance().getReference();
                db.child(UID).child(countstr).setValue(details);

                Intent intent = new Intent(DonationPage.this, donorEnd.class);
                intent.putExtra("volunteer", rbtnstr);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(DonationPage.this, MainActivity.class);
                startActivity(intToMain);
                finish();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(DonationPage.this, com.example.fooddonation.editProfile.class);
                startActivity(edit);
            }
        });
    }
}