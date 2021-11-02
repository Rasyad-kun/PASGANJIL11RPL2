package com.example.pasganjil11rpl2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment{
    public static String userID;
    private TextView txt_username, txt_email, logout;
    private ImageView editprofile1, editprofile2, editprofile3, profilepic;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private ProgressBar progressBar;
    public static final String TAG = "TAG";
    private ListenerRegistration registration;
    private StorageReference storageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        txt_username = v.findViewById(R.id.txt_username);
        txt_email = v.findViewById(R.id.txt_email);
        profilepic = v.findViewById(R.id.profilepic);
        logout = v.findViewById(R.id.log_out);
        progressBar = v.findViewById(R.id.progressbar);

        editprofile1 = v.findViewById(R.id.editlogo1);
        editprofile2 = v.findViewById(R.id.editlogo2);
        editprofile3 = v.findViewById(R.id.editlogo3);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(userID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilepic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        DocumentReference docref = fStore.collection("users").document(userID);

        registration = docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists() && value != null) {
                    txt_username.setText(value.getString("username"));
                    txt_email.setText(value.getString("email"));
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Log.d(TAG, "Such document doesn't exist");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        editprofile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile();
            }
        });

        editprofile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile();
            }
        });

        editprofile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        return v;
    }

    private void EditProfile() {
        registration.remove();
        startActivity(new Intent(getContext(), EditProfile.class));
        getActivity().finish();
    }

    private void Logout() {
        registration.remove();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }
}
