package com.example.ideaskill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Sigup_Form extends AppCompatActivity {

    EditText signupFirstName, signupLastName, signupEmail, signupPassword;
    Button signup;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup__form);
        signupFirstName = findViewById(R.id.signupFirstName);
        signupLastName = findViewById(R.id.signupLastName);
        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        signup = findViewById(R.id.signup);
        progressBar = findViewById(R.id.progressBar);
        final View relativeLayout_signup = findViewById(R.id.relativeLayout_signup);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");


        firebaseAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = signupFirstName.getText().toString().trim();
                final String lastName = signupLastName.getText().toString().trim();
                final String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();

                if (TextUtils.isEmpty(firstName)) {
                    Snackbar.make(relativeLayout_signup,"Enter First Name",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    Snackbar.make(relativeLayout_signup,"Enter Last Name",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(relativeLayout_signup,"Enter Email",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(relativeLayout_signup,"Enter Password",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Snackbar.make(relativeLayout_signup,"Password too short",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Sigup_Form.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);

                               if (task.isSuccessful()) {

/*                                    User information = new User(
                                            firstName,
                                            lastName,
                                            email
                                    );
*/
                                    FirebaseUser user=firebaseAuth.getCurrentUser();
                                    if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                        String email=user.getEmail();
                                        String Uid=user.getUid();
                                        HashMap<Object,String> hashMap=new HashMap<>();
                                        hashMap.put("email",email);
                                        hashMap.put("Uid",Uid);
                                        hashMap.put("First Name",firstName);
                                        hashMap.put("Last Name",lastName);
                                        hashMap.put("phone","");
//                                        hashMap.put("image","");
                                        hashMap.put("Skills","");
                                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                                        DatabaseReference reference=database.getReference("User");
                                        reference.child(Uid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                sendEmailVerification();
                                            }
                                        });
                                    }


                            } else {
                                    Toast.makeText(Sigup_Form.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });

            }
        });
    }
    private void sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Sigup_Form.this, "Verification Mail Sent", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(),Login_Form.class));
                    }
                    else{
                        Toast.makeText(Sigup_Form.this, "Server is Busy", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),First_Page.class));
        //finishAffinity();
        /*Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
        finish();*/
    }
}
