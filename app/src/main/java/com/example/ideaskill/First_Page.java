package com.example.ideaskill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class First_Page extends AppCompatActivity {
    Button firstPageSignup,firstPageLogin;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        firstPageLogin = findViewById(R.id.firstPageLogin);
        firstPageSignup = findViewById(R.id.firstPageSignup);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            this.finish();
        } else {

            firstPageSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), Sigup_Form.class));
                }
            });
            firstPageLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), Login_Form.class));
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
