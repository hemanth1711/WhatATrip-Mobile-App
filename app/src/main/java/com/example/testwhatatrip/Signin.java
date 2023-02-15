package com.example.testwhatatrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signin extends AppCompatActivity {
    Button newuser,Login,ForgotPassword;
    TextInputEditText Email,Password;
    FirebaseAuth FAuth;
    AlertDialog.Builder resetAlert;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        newuser = findViewById(R.id.newUser);
        Email = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        ForgotPassword = findViewById(R.id.forgotPassword);
        Login = findViewById(R.id.loginbtn);
        FAuth = FirebaseAuth.getInstance();
        resetAlert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this,Register.class);
                startActivity(intent);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText().toString().isEmpty())
                {
                    Toast.makeText(Signin.this,"Enter Email",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Password.getText().toString().isEmpty()){
                        Toast.makeText(Signin.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Checking Email and password using firebase
                        FAuth.signInWithEmailAndPassword(Email.getText().toString(),Password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(Signin.this,Home.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                                Toast.makeText(Signin.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alert the dialog
                View view = inflater.inflate(R.layout.email_reset,null);
                resetAlert.setTitle("Reset The ForgotPassword").
                        setMessage("Enter Your Email To Reset The password").
                        setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText email = view.findViewById(R.id.EmailReset);
                                if (email.getText().toString().isEmpty()){
                                    email.setError("Email is required");
                                    return;
                                }
                                FAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Signin.this,"Reset link sent successfully",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(Signin.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("cancle", null).setView(view).create().show();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }
    }
}