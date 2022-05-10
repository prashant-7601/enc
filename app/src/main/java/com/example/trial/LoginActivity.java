package com.example.trial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView createNewAccount;
    EditText email, password;
    Button login;
    String mailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressdialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createNewAccount = findViewById(R.id.ExistingAccount);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        email = findViewById(R.id.EmailAddress);
        password = findViewById(R.id.Password);
        login = findViewById(R.id.LoginBtn);
        progressdialog = new ProgressDialog(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        performLogin();
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String email_id = email.getText().toString();
        String password_input = password.getText().toString();
        email_id = "prashantarya070601@gmail.com";
        password_input = "prashu01";
        if(!email_id.matches(mailPattern)){
            email.setError("Email address not valid!");
        }
        else if(password_input.isEmpty() || password_input.length()<6){
            password.setError("Password should be longer than 5 characters!");
        }
        else {
            progressdialog.setMessage("Logging in...");
            progressdialog.setTitle("Log in");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.show();

            mAuth.signInWithEmailAndPassword(email_id, password_input).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressdialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Registration Successful", Toast.LENGTH_SHORT);
                        sendUserToNextActivity();
                    }
                    else{
                        progressdialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}