package com.example.ideaskill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    CircleImageView profile_image_header;
    TextView name_header;
    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    List<ModelPost> postList;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    AdapterPost adapterPost;
    Post p=new Post();
       @Override
    protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_home);
           postList =new ArrayList<>();
           //checkConnection();
           GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                   .requestIdToken(getString(R.string.default_web_client_id))
                   .requestEmail()
                   .build();
           mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
           drawerLayout=findViewById(R.id.drawerLayout);
           navigationView=findViewById(R.id.navigationView);
           View hView=navigationView.inflateHeaderView(R.layout.home_header);
           name_header=hView.findViewById(R.id.name_header);
           profile_image_header=hView.findViewById(R.id.profile_image_header);
           TabLayout tabLayout=findViewById(R.id.tabs);
           recyclerView=findViewById(R.id.postsRecyclerview);
           ViewPager viewPager=findViewById(R.id.viewPager);
           TabPagerAdapter tabPagerAdapter=new TabPagerAdapter(getSupportFragmentManager());
           viewPager.setAdapter(tabPagerAdapter);
           tabLayout.setupWithViewPager(viewPager);
           firebaseAuth = FirebaseAuth.getInstance();
           FirebaseUser user = firebaseAuth.getCurrentUser();
           firebaseDatabase = FirebaseDatabase.getInstance();
       //    dialog.setMessage("Doing something...");
         //  dialog.show();

           databaseReference = firebaseDatabase.getReference("User");
           Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
           query.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   for(DataSnapshot ds:dataSnapshot.getChildren()){
                       String firstName=""+ds.child("First Name").getValue();
                       String lastName=""+ds.child("Last Name").getValue();
                       String image=""+ds.child("image").getValue();
                        name_header.setText(firstName+" "+lastName);
                       try {
                           Picasso.with(Home.this).load(image).into(profile_image_header);
                       }catch (Exception e){
                           Picasso.with(Home.this).load(R.drawable.combined).into(profile_image_header);
                       }
                   }
                   //dialog.hide();
               }
               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
           toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
           drawerLayout.addDrawerListener(toggle);
           toggle.syncState();
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setElevation(0);
           navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
               @Override
               public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                   int id=menuItem.getItemId();
                   if(id==R.id.nav_signout){
                       signOut();
                   }
                   if(id==R.id.nav_myprofile){
                       startActivity(new Intent(getApplicationContext(),EditProfile.class));
                   }
                   return true;
               }
           });

       }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);


        return true;
    }

       @Override
       public boolean onOptionsItemSelected(MenuItem item){

           int id=item.getItemId();
           if(id== R.id.searchButton){

           }
           return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
       }

       public void signOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task){
                        startActivity(new Intent(getApplicationContext(),Login_Form.class));
                        mGoogleSignInClient.signOut();
                        FirebaseAuth.getInstance().signOut();

                    }
                });
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
