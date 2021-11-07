package com.example.pasganjil11rpl2.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasganjil11rpl2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private RelativeLayout signInButton;
    private AppCompatButton createAccount;
    private TextView login_button;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private ProgressBar progressBar;
    private String Email, Username, Password, userID;
    private GoogleSignInClient googleSignInClient;
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

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

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

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, SIGN_IN);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            if (task.isSuccessful()) {
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Toast.makeText(MainActivity.this, "Error has occurred"
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT ).show();
                Log.d(TAG, "Error has occurred: " + task.getException());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Sign In Successful");
                            Username = fAuth.getCurrentUser().getDisplayName();
                            Email = fAuth.getCurrentUser().getEmail();
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
                            googleSignInClient.signOut();
                            Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Error has occurred: ", task.getException());
                        }
                    }
                });
    }

}