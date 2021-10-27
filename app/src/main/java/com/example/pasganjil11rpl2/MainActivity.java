package com.example.pasganjil11rpl2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private RelativeLayout signInButton;
    private Button createAccount;
    private String Email, Username, Password, Profilepic;
    private GoogleApiClient googleApiClient;
    private static final int SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText username = findViewById(R.id.Username);
        EditText email = findViewById(R.id.Email);
        EditText password = findViewById(R.id.Password);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,
                this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        signInButton = findViewById(R.id.googlelogin);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN);
            }
        });

        createAccount = findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username = username.getText().toString();
                Email = email.getText().toString();
                Password = password.getText().toString();
                if (!Username.trim().isEmpty() && !Email.trim().isEmpty() && !Password.trim().isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        if (Password.length() >= 8) {
                            Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                            intent.putExtra("username", Username);
                            intent.putExtra("email", Email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Your Password is too short",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect Email",
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

        if (requestCode == SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}