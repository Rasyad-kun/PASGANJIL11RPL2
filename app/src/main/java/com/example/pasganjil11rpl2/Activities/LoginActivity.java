package com.example.pasganjil11rpl2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasganjil11rpl2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private String Email, Password;
    private AppCompatButton login_button;
    private TextView create_account, forgot_password;
    private ProgressBar progressBar;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        login_button = findViewById(R.id.login_button);
        create_account = findViewById(R.id.create_account);
        forgot_password = findViewById(R.id.forgot_password);
        progressBar = findViewById(R.id.progressbar);
        fAuth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = email.getText().toString().trim();
                Password = password.getText().toString().trim();
                if (!Email.isEmpty() && !Password.isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        if (Password.length() >= 8) {
                            progressBar.setVisibility(View.VISIBLE);

                            fAuth.signInWithEmailAndPassword(Email, Password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "Successfully Login!", Toast.LENGTH_SHORT)
                                                        .show();
                                                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Error has occured: " + task.getException()
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

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PasswordReset.class);
                startActivity(intent);
                finish();
            }
        });
    }
}