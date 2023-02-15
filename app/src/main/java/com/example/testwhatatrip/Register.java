package com.example.testwhatatrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {
    Button moveToSignInBtn,SignUpbtn;
    TextInputEditText FullName,Email,Phone,Password;
    FirebaseAuth FAuth;
    FirebaseDatabase RootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // Initializing firebase authentication
        FAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        // Assignning buttons
        moveToSignInBtn = findViewById(R.id.movetoSigninbtn);
        SignUpbtn = findViewById(R.id.SignUpbtn);
        // Assignning Variables
        FullName = findViewById(R.id.FullName);
        Email = findViewById(R.id.Email);
        Phone= findViewById(R.id.phoneNo);
        Password = findViewById(R.id.Password);
        moveToSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Signin.class);
                startActivity(intent);
            }
        });
        SignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FullName.getText().toString().isEmpty()) {
                    Toast.makeText(Register.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Email.getText().toString().isEmpty()) {
                        Toast.makeText(Register.this, "Enter Email Adderess", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Email.getText().toString().matches(emailPattern)) {
                            if (Phone.length()!=10){
                                Toast.makeText(Register.this, "The Phone Number Should have 10 numbers", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if (Password.getText().toString().isEmpty()){
                                    Toast.makeText(Register.this,"Password should not be empty",Toast.LENGTH_SHORT).show();
                                }
                                else {
//                                    Toast.makeText(Register.this, "Signup successfull", Toast.LENGTH_SHORT).show();
                                    FAuth.createUserWithEmailAndPassword(Email.getText().toString(),Password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            RootNode = FirebaseDatabase.getInstance();
                                            reference = RootNode.getReference("users");
                                            String full_name = FullName.getText().toString();
                                            String email = Email.getText().toString();
                                            String phone = Phone.getText().toString();
                                            UserHelper helperClass = new UserHelper(full_name,email,phone);
                                            reference.child(FAuth.getUid()).setValue(helperClass);

                                            /////////////////////////////////////////   USER DATA FIELDS /////////////////////////
                                            List<String> documentNames = new ArrayList<>();
                                            documentNames.add("MY_FAVORITES");
                                            documentNames.add("MY_RATINGS");

                                            Map<String, Object> wishList = new HashMap<>();
                                            wishList.put("list_size", (long) 0);

                                            Map<String, Object> ratingsmap = new HashMap<>();
                                            ratingsmap.put("list_size", (long) 0);

                                            List<Map<String, Object>> documentFields = new ArrayList<>();
                                            documentFields.add(wishList);
                                            documentFields.add(ratingsmap);

                                            CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(FAuth.getUid())
                                                    .collection("USER_DATA");
//                                            Map<String, Object> user_name = new HashMap<>();
//                                            user_name.put("user_name",full_name);
//                                            firebaseFirestore.collection("USERS").document(FAuth.getUid()).set(user_name).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull  Task<Void> task) {
//                                                    if (task.isSuccessful())
//                                                    {
//                                                        Toast.makeText(Register.this,"UserName added successfully",Toast.LENGTH_SHORT).show();
//                                                    }
//                                                    else
//                                                    {
//                                                        Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                                                    }
//
//                                                }
//                                            });
                                            for (int x=0; x < documentNames.size(); x++) {
                                                final int finalX = x;
                                                userDataReference.document(documentNames.get(x))
                                                        .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull  Task<Void> task) {
                                                        if(task.isSuccessful()) {
//
                                                            if(finalX == documentNames.size()-1) {
                                                                Intent intent = new Intent(Register.this, Home.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(Register.this,error,Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });

                                            }


//                                            firebaseFirestore.collection("USERS").document(FAuth.getUid())
//                                                    .collection("USER_DATA").document("MY_FAVORITES").set(wishList).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull  Task<Void> task) {
//                                                    if(task.isSuccessful()) {
////                                                        Toast.makeText(Register.this, "Created favorites in firebase", Toast.LENGTH_SHORT).show();
//                                                        Intent intent = new Intent(Register.this, Home.class);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    }
//                                                    else
//                                                    {
//                                                        String error = task.getException().getMessage();
//                                                        Toast.makeText(Register.this,error,Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull  Exception e) {
                                            Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }
                        } else {
                            Toast.makeText(Register.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}