package com.example.ideaskill;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_SHORT;

public class EditProfile extends AppCompatActivity {

    CircleImageView profile_image, profile_image_header;
    TextView firstName_editProfile,lastName_editProfile,email_editProfile,skills_editProfile;
    private Uri imageUri;
    Button save_editProfile, changePhotoButton;
    FloatingActionButton editButton;
    private final int PICK_IMAGE = 1;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageTask uploadTask;
    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPost adapterPost;
    ProgressBar progressBar_editProfile;
    String cameraPermission[];
    String storagePermission[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profile_image = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
        save_editProfile = findViewById(R.id.save_editProfile);
        firstName_editProfile=findViewById(R.id.firstName_editProfile);
        //lastName_editProfile=findViewById(R.id.lastName_editProfile);
        email_editProfile=findViewById(R.id.email_editProfile);
        skills_editProfile=findViewById(R.id.skills_editProfile);
        changePhotoButton = findViewById(R.id.changePhotoButton);
        editButton=findViewById(R.id.editButton);
        progressBar_editProfile=findViewById(R.id.progressBar_editProfile);
        profile_image_header = findViewById(R.id.profile_image_header);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String firstName=""+ds.child("First Name").getValue();
                    String lastName=""+ds.child("Last Name").getValue();
                    String email=""+ds.child("email").getValue();
                    String skills=""+ds.child("Skills").getValue();
                    String image=""+ds.child("image").getValue();
                    firstName_editProfile.setText(firstName+" "+lastName);
                    //lastName_editProfile.setText(lastName);
                    email_editProfile.setText(email);
                    skills_editProfile.setText(skills);
                    try {
                        Picasso.with(EditProfile.this).load(image).into(profile_image);
                    }catch (Exception e){
                        Picasso.with(EditProfile.this).load(R.drawable.combined).into(profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView=findViewById(R.id.editProfilePostsRecyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        postList =new ArrayList<>();
        //  dialog.show();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);


            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditProfileDialog();
            }
        });

}

    private void loadPosts() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        ref.orderByChild("uEmail").equalTo(user.getEmail())
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                    adapterPost=new AdapterPost(getApplicationContext(),postList);
                    recyclerView.setAdapter(adapterPost);
                }
                //dialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(this, "Uploading in progress", Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();

            }
        }
    }

    private void uploadImage() {

        save_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_editProfile.setVisibility(View.VISIBLE);
                if (imageUri != null) {

                    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    final StorageReference fileReference = storageReference.child("images/" + firebaseUser.getUid());
                    fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    final String uri1=uri.toString();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("image", uri1);
                                    progressBar_editProfile.setVisibility(View.GONE);
                                    databaseReference = FirebaseDatabase.getInstance().getReference("User");
                                    databaseReference.child(firebaseUser.getUid()).updateChildren(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Picasso.with(EditProfile.this).load(uri1).into(profile_image);

                                                    Toast.makeText(EditProfile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditProfile.this, "Can't Upload Image", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
//                            final Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                            while (!uriTask.isSuccessful()) {
//                                final Uri downloadUri = uriTask.getResult();
//                                if (uriTask.isSuccessful()) {
//                                    HashMap<String, Object> hashMap = new HashMap<>();
//                                    hashMap.put("image", downloadUri);
//                                    progressBar_editProfile.setVisibility(View.GONE);
//
//                                    databaseReference.child(firebaseUser.getUid()).updateChildren(hashMap)
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Picasso.with(EditProfile.this).load(downloadUri).into(profile_image);
//                                                    Toast.makeText(EditProfile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(EditProfile.this, "Can't Upload Image", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                } else {
//                                    Toast.makeText(EditProfile.this, "Can'Upload Image", Toast.LENGTH_SHORT).show();
//                                }
//                            }
                        }
                    });
                }
            }
        });
    }
                          /*  return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                               // Uri downloadUri=task.getResult();
                                String mUri=fileReference.toString();

                                databaseReference=FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
                                HashMap<String,Object> map=new HashMap<>();
                                map.put("image",mUri);
                                databaseReference.updateChildren(map);
                                progressBar_editProfile.setVisibility(View.GONE);
                                Bitmap bitmap ;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                    //profile_image.setImageBitmap(bitmap);
                                    fileReference.child("image/"+firebaseUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Picasso.get().load(uri).into(profile_image);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditProfile.this, "Can't Upload Image", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }else{
                                Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    Toast.makeText(EditProfile.this, "Select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    */


            private void showEditProfileDialog() {
                String options[] = {"Edit First Name", "Edit Last Name", "Edit Skills"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Action");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            showNameAndSkillUpdateDialog("First Name");
                        } else if (i == 1) {
                            showNameAndSkillUpdateDialog("Last Name");
                        } else if (i == 2) {
                            showNameAndSkillUpdateDialog("Skills");
                        }
                    }
                });
                builder.create().show();
            }

            private void showNameAndSkillUpdateDialog(final String key) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Edit " + key);
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10, 10, 10, 10);
                final EditText editText = new EditText(this);
                editText.setHint("Enter " + key);
                linearLayout.addView(editText);
                builder.setView(linearLayout);
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String value = editText.getText().toString().trim();
                        if (!TextUtils.isEmpty(value)) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put(key, value);
                            databaseReference.child(firebaseUser.getUid()).updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(EditProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditProfile.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(EditProfile.this, "Enter " + key, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        }