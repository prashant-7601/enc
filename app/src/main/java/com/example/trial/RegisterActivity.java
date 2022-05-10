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

public class RegisterActivity extends AppCompatActivity {

    TextView existingAccount;
    EditText email, password, confirmPassword;
    Button register;
    String mailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressdialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        existingAccount = findViewById(R.id.ExistingAccount);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        email = findViewById(R.id.EmailAddress);
        password = findViewById(R.id.Password);
        confirmPassword = findViewById(R.id.ConfirmPassword);
        register = findViewById(R.id.RegisterBtn);
        progressdialog = new ProgressDialog(RegisterActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        existingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });
    }

    private void PerformAuth() {
        String email_id = email.getText().toString();
        String password_input = password.getText().toString();
        String confirmPassword_input = confirmPassword.getText().toString();

        if(!email_id.matches(mailPattern)){
            email.setError("Email address not valid!");
        }
        else if(password_input.isEmpty() || password_input.length()<6){
            password.setError("Password should be longer than 5 characters!");
        }
        else if(!password_input.equals(confirmPassword_input)){
            confirmPassword.setError("Password mismatch!");
        }
        else {
            progressdialog.setMessage("Please wait while registering...");
            progressdialog.setTitle("Registration");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.show();

            mAuth.createUserWithEmailAndPassword(email_id, password_input).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressdialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT);
                        sendUserToLoginActivity();
                    }
                    else{
                        progressdialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT);
                    }
                }
            });
        }

    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}