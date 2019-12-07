package com.example.ideaskill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class AddPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

//    EditText post_dateText,post_dateText2;
    //ImageButton post_date;
    EditText post_aim;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference userDbRef;
    String name,email,uid;
    Calendar calendar;
    Button postButton;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Create Post");
        firebaseAuth=FirebaseAuth.getInstance();
        userDbRef= FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=userDbRef.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {

                    email = "" + ds.child("email").getValue();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        postButton=findViewById(R.id.postButton);
        post_aim=findViewById(R.id.post_aim);
       // post_date=findViewById(R.id.post_date);
//        post_dateText=findViewById(R.id.post_dateText);
//        post_dateText2=findViewById(R.id.post_dateText2);
//        post_dateText.setEnabled(false);
//        post_dateText2.setEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        final Spinner spinner=findViewById(R.id.post_typeSpinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Types,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
//        post_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                calendar=Calendar.getInstance();
//                int day=calendar.get(Calendar.DAY_OF_MONTH);
//                int month=calendar.get(Calendar.MONTH);
//                int year=calendar.get(Calendar.YEAR);
//
//                datePickerDialog=new DatePickerDialog(AddPostActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
//                        post_dateText.setText(mDay+"/"+(mMonth+1)+"/"+mYear);
//                    }
//                },day,month,year);
//                datePickerDialog.show();
//            }
//        });
    }

    private void uploadData(final String aim, final String type) {


        firebaseAuth=FirebaseAuth.getInstance();
        userDbRef= FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=userDbRef.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String firstName = "" + ds.child("First Name").getValue();
                    String lastName = "" + ds.child("Last Name").getValue();
                    String timeStamp=String.valueOf(System.currentTimeMillis());
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    String Uid=user.getUid();
                    String email=user.getEmail();
                    String image="" + ds.child("image").getValue();
                    String uName=firstName+" "+lastName;
                    HashMap<Object, String> hashMap=new HashMap<>();
                    hashMap.put("pId",Uid);
                    hashMap.put("profileImage",image);
                    hashMap.put("uName",uName);
                    hashMap.put("uEmail",email);
                    hashMap.put("uAim",aim);
                    hashMap.put("uId",timeStamp);
                    hashMap.put("uTimeOfPost",timeStamp);
                    hashMap.put("uType",type);
                    hashMap.put("Progress","Devloper Needed");
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
                    ref.child(timeStamp).setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(AddPostActivity.this,Home.class));
                                    Toast.makeText(AddPostActivity.this, "Published", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddPostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_post_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.postButton){
            Spinner spinner=findViewById(R.id.post_typeSpinner);
            String aim=post_aim.getText().toString().trim();
            String type=spinner.getSelectedItem().toString();
            if(TextUtils.isEmpty(aim)){
                Toast.makeText(AddPostActivity.this, "Enter Aim", Toast.LENGTH_SHORT).show();
            }
            if(TextUtils.isEmpty(type)){
                Toast.makeText(AddPostActivity.this, "Enter Project Type", Toast.LENGTH_SHORT).show();
            }
            else{
                uploadData(aim,type);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String type=adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
