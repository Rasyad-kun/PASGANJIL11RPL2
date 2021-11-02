package com.example.pasganjil11rpl2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.perfmark.Tag;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private RelativeLayout signInButton;
    private AppCompatButton createAccount;
    private TextView login_button;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private ProgressBar progressBar;
    private String Email, Username, Password, userID;
    private GoogleApiClient googleApiClient;
    private static final int SIGN_IN = 1;
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText username = findViewById(R.id.Username);
        EditText email = findViewById(R.id.Email);
        EditText password = findViewById(R.id.Password);
        signInButton = findViewById(R.id.googlelogin);
        login_button = findViewById(R.id.login);
        createAccount = findViewById(R.id.create_account);
        progressBar = findViewById(R.id.progressbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,
                this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        if (fAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
            startActivity(intent);
            finish();
        }

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username = username.getText().toString().trim();
                Email = email.getText().toString();
                Password = password.getText().toString().trim();
                if (!Username.isEmpty() && !Email.trim().isEmpty() && !Password.isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        if (Password.length() >= 8) {
                            progressBar.setVisibility(View.VISIBLE);

                            fAuth.createUserWithEmailAndPassword(Email, Password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_SHORT)
                                                        .show();
                                                userID = fAuth.getCurrentUser().getUid();
                                                DocumentReference docref = fStore.collection("users").document(userID);
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("username", Username);
                                                user.put("email", Email);
                                                docref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d(TAG, "onSuccess: user profile is created for "+ userID);
                                                    }
                                                });
                                                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(MainActivity.this, "Error has occured: " + task.getException()
                                                        .getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Password is too short",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
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
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}