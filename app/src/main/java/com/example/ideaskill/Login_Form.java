package com.example.ideaskill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Login_Form extends AppCompatActivity {

    EditText loginEmail,loginPassword;
    Button login;
  //  SignInButton signInButton;
    //GoogleSignInClient mGoogleSignInClient;
   // private int RC_SIGN_IN=11;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__form);
        login = findViewById(R.id.login);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
     //   signInButton = findViewById(R.id.signinButton);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            this.finish();
        }

           /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            onStart();*/
//            signInButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    signIn();
//                }
//            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String email = loginEmail.getText().toString().trim();
                    String password = loginPassword.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        Snackbar.make(findViewById(R.id.relativeLayout_login),"Enter Email",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Snackbar.make(findViewById(R.id.relativeLayout_login),"Enter Password",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() < 6) {
                        Snackbar.make(findViewById(R.id.relativeLayout_login),"Password too Short",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login_Form.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        checkEmailVerification();
                                    } else {
                                        Toast.makeText(Login_Form.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }
//        private void signIn () {
//
//            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//            startActivityForResult(signInIntent, RC_SIGN_IN);
//        }


//        @Override
//        public void onActivityResult ( int requestCode, int resultCode, Intent data){
//            super.onActivityResult(requestCode, resultCode, data);
//
//            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//            if (requestCode == RC_SIGN_IN) {
//                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                try {
//                    // Google Sign In was successful, authenticate with Firebase
//                    GoogleSignInAccount account = task.getResult(ApiException.class);
//                    firebaseAuthWithGoogle(account);
//                } catch (ApiException e) {
//                    Log.w("Login_Page", "Google sign in failed", e);
//                    Snackbar.make(findViewById(R.id.relativeLayout_login),"Error",Snackbar.LENGTH_SHORT).show();
//                }
//            }
//        }
//
//        private void firebaseAuthWithGoogle (final GoogleSignInAccount acct){
//            Log.d("Account", "firebaseAuthWithGoogle:" + acct.getId());
//
//            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//            firebaseAuth.signInWithCredential(credential)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                FirebaseUser user = firebaseAuth.getCurrentUser();
//                              String firstName=user.getDisplayName();
//                              String email=user.getEmail();
//                              String Uid=user.getUid();
//                              HashMap<Object,String> hashMap=new HashMap<>();
//                              hashMap.put("email",email);
//                              hashMap.put("Uid",Uid);
//                              hashMap.put("First Name",firstName);
//                              hashMap.put("Last Name","");
//                              hashMap.put("phone","");
//                              hashMap.put("image","");
//                              hashMap.put("Skills","");
//                              FirebaseDatabase database=FirebaseDatabase.getInstance();
//                              DatabaseReference reference=database.getReference("User");
//                              reference.child(Uid).setValue(hashMap);
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d("Success", "signInWithCredential:success");
//                                updateUI(user);
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w("Error", "signInWithCredential:failure", task.getException());
//                                Snackbar.make(findViewById(R.id.relativeLayout_login),"Error",Snackbar.LENGTH_SHORT).show();
//
//                            }
//
//
//                        }
//                    });
//
//        }

//        private void updateUI (FirebaseUser user){
//
//            Intent intent = new Intent(this, Home.class);
//            startActivity(intent);
//
//
//        }
        private void checkEmailVerification () {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            boolean emailFlag = firebaseUser.isEmailVerified();
            if (emailFlag) {
                finish();
                startActivity(new Intent(getApplicationContext(), Home.class));
            } else {
                Toast.makeText(this, "Verify your Email", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
            }
        }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),First_Page.class));//finishAffinity();
    }
}
