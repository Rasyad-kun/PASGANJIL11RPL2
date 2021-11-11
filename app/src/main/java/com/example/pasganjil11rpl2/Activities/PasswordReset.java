package com.example.pasganjil11rpl2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasganjil11rpl2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {

    private EditText txt_email;
    private String email;
    private AppCompatButton sendEmail;
    private TextView goBack;
    private FirebaseAuth fAuth;
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        getSupportActionBar().hide();

        txt_email = findViewById(R.id.Email);
        sendEmail = findViewById(R.id.reset_button);
        goBack = findViewById(R.id.cancel_reset);

        fAuth = FirebaseAuth.getInstance();

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txt_email.getText().toString();
                if (!email.trim().isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PasswordReset.this, "Reset Link is Sent",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PasswordReset.this, "Error has occurred: "
                                        + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Error: " + e.getMessage());
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid Email",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please type in your Email",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordReset.this, LoginActivity.class));
                finish();
            }
        });
    }
}