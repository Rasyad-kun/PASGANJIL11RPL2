package com.example.pasganjil11rpl2.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasganjil11rpl2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfile extends AppCompatActivity {

    private EditText txt_username, txt_email;
    private TextView cancel, apply_changes;
    private ImageView profile_pic;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID, Username, Email;
    private FirebaseUser fUser;
    private static final int GET_IMAGE = 3;
    public static final String TAG = "TAG";
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();

        txt_username = findViewById(R.id.txt_username);
        txt_email = findViewById(R.id.txt_email);
        profile_pic = findViewById(R.id.profilepic);
        apply_changes = findViewById(R.id.update);
        cancel = findViewById(R.id.cancel);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        StorageReference profRef = storageRef.child(userID);
        profRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile_pic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        DocumentReference docref = fStore.collection("users").document(userID);

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.
                        EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, GET_IMAGE);
            }
        });

        docref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                txt_username.setText(value.getString("username"));
                txt_email.setText(value.getString("email"));
            }
        });

        apply_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username = txt_username.getText().toString();
                Email = txt_email.getText().toString();
                if (!Username.isEmpty() && !Email.trim().isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        fUser.updateEmail(Email)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        docref.update("username", Username);
                                        docref.update("email", Email);
                                        Toast.makeText(EditProfile.this, "Update Successful"
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfile.this, "Error has occurred: " +
                                        e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(EditProfile.this, NavigationActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Email",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in the boxes",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, NavigationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri image_uri = data.getData();
                //profile_pic.setImageURI(image_uri);
                
                uploadImagetoFirebase(image_uri);
            }
        }

    }

    private void uploadImagetoFirebase(Uri image_uri) {
        StorageReference fileRef = storageRef.child(userID);
        fileRef.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profile_pic);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Error has occurred: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Failed to upload image", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}